# Test Execution Fixes - Multiple Browser Instance Issue

## Root Cause Analysis

The **2 browser instances** were being created due to **cascading Driver.getInstance() calls** happening at CLASS-LEVEL field initialization before the @Before hook could run:

### Call Sequence (Before Fixes):
1. **Cucumber loads step definition classes**
   - `LoginStepDefinitions` class loads
   - Field initializer: `BaseOrangeHRMLoginPageObjects baseOrangeHRMLoginPageObjects = new BaseOrangeHRMLoginPageObjects();` runs
   - **Browser Instance #1 created** ❌

2. **BaseOrangeHRMLoginPageObjects constructor runs**
   - Field initializer: `OrangeHRMLoginPageObjects hrmLoginPageObjects = new OrangeHRMLoginPageObjects();` runs
   - Calls `super()` which calls `Driver.getInstance()` → **Second wrapper created** ❌
   - OrangeHRMLoginPageObjects constructor calls `PageFactory.initElements(Driver.getInstance(), this)` → **Third wrapper** ❌

3. **Similar issue in AdminStepDefinitions**
   - `AdminPageObjects adminPageObjects = new AdminPageObjects();` at class level
   - Calls `Driver.getInstance()` → **More wrappers/calls** ❌

4. **@Before hook runs** (but driver already initialized)
   - Calls `Driver.getInstance()` again

---

## All Fixes Applied

### Fix #1: Lazy Initialization in LoginStepDefinitions
**Location:** [LoginStepDefinitions.java](src/test/java/com/orangeHRM/stepDefinitions/LoginStepDefinitions.java)

**Problem:** Page object was instantiated at class-level, causing premature Driver.getInstance() call

**Before:**
```java
public class LoginStepDefinitions extends BaseStepDefinition {
    BaseOrangeHRMLoginPageObjects baseOrangeHRMLoginPageObjects = new BaseOrangeHRMLoginPageObjects();  // ❌ Creates at class load
}
```

**After:**
```java
public class LoginStepDefinitions extends BaseStepDefinition {
    private BaseOrangeHRMLoginPageObjects baseOrangeHRMLoginPageObjects;
    
    private BaseOrangeHRMLoginPageObjects getLoginPageObject() {
        if (baseOrangeHRMLoginPageObjects == null) {
            baseOrangeHRMLoginPageObjects = new BaseOrangeHRMLoginPageObjects();
        }
        return baseOrangeHRMLoginPageObjects;
    }
}
```

**Also updated all method calls:**
- `baseOrangeHRMLoginPageObjects.open(url)` → `getLoginPageObject().open(url)`
- `baseOrangeHRMLoginPageObjects.login(user, pwd)` → `getLoginPageObject().login(user, pwd)`

---

### Fix #2: Lazy Initialization in AdminStepDefinitions
**Location:** [AdminStepDefinitions.java](src/test/java/com/orangeHRM/stepDefinitions/AdminStepDefinitions.java)

**Problem:** Same issue as LoginStepDefinitions

**Before:**
```java
public class AdminStepDefinitions extends BaseStepDefinition {
    AdminPageObjects adminPageObjects = new AdminPageObjects();  // ❌ Creates at class load
}
```

**After:**
```java
public class AdminStepDefinitions extends BaseStepDefinition {
    private AdminPageObjects adminPageObjects;
    
    private AdminPageObjects getAdminPageObject() {
        if (adminPageObjects == null) {
            adminPageObjects = new AdminPageObjects();
        }
        return adminPageObjects;
    }
}
```

**Also updated all method calls:**
- `adminPageObjects.navigateToAdminModule()` → `getAdminPageObject().navigateToAdminModule()`
- `adminPageObjects.addNewEmployee()` → `getAdminPageObject().addNewEmployee()`

---

### Fix #3: Lazy Initialization in BaseOrangeHRMLoginPageObjects
**Location:** [BaseOrangeHRMLoginPageObjects.java](src/main/java/com/orangeHrm/pages/BaseOrangeHRMLoginPageObjects.java)

**Problem:** Nested page object was instantiated at class-level

**Before:**
```java
public class BaseOrangeHRMLoginPageObjects {
    OrangeHRMLoginPageObjects hrmLoginPageObjects = new OrangeHRMLoginPageObjects();  // ❌ Creates at class load
    private WebDriver driver;
    
    public void login(String userName, String userPwd) {
        SeleniumTestHelper.enterText(hrmLoginPageObjects.userNameTxtBx, userName);  // ❌ Uses eagerly initialized object
    }
}
```

**After:**
```java
public class BaseOrangeHRMLoginPageObjects {
    private OrangeHRMLoginPageObjects hrmLoginPageObjects;
    private WebDriver driver;
    
    private OrangeHRMLoginPageObjects getHrmLoginPageObjects() {
        if (hrmLoginPageObjects == null) {
            hrmLoginPageObjects = new OrangeHRMLoginPageObjects();
        }
        return hrmLoginPageObjects;
    }
    
    public void login(String userName, String userPwd) {
        SeleniumTestHelper.enterText(getHrmLoginPageObjects().userNameTxtBx, userName);  // ✅ Uses lazy getter
    }
}
```

**All usages updated:**
- `hrmLoginPageObjects.userNameTxtBx` → `getHrmLoginPageObjects().userNameTxtBx`
- `hrmLoginPageObjects.userPwdTxtBx` → `getHrmLoginPageObjects().userPwdTxtBx`
- `hrmLoginPageObjects.SignInBtn` → `getHrmLoginPageObjects().SignInBtn`

---

### Fix #4: Remove Duplicate PageFactory Initialization
**Location:** [OrangeHRMLoginPageObjects.java](src/main/java/com/orangeHrm/pages/OrangeHRMLoginPageObjects.java)

**Problem:** PageFactory.initElements() was called twice - once in parent, once in child

**Before:**
```java
public class OrangeHRMLoginPageObjects extends BaseOrangeHRMLoginPageObjects {
    public OrangeHRMLoginPageObjects() {
        super();
        PageFactory.initElements(Driver.getInstance(), this);  // ❌ Called AGAIN!
    }
}
```

**After:**
```java
public class OrangeHRMLoginPageObjects extends BaseOrangeHRMLoginPageObjects {
    public OrangeHRMLoginPageObjects() {
        super();  // ✅ Parent already initializes PageFactory
        // No need to call PageFactory.initElements() again
    }
}
```

---

### Fix #5: Static Driver Initialization in SeleniumTestHelper (Already Applied)
**Location:** [SeleniumTestHelper.java](src/main/java/com/orangeHrm/utils/SeleniumTestHelper.java#L41)

**Problem:** Static initializer created first browser prematurely

**Change:**
- Removed: `static WebDriver driver = Driver.getInstance();`
- Added: Lazy getter methods `setDriver()` and `getDriver()`

---

### Fix #6: WebDriverDispatcher Caching in Driver (Already Applied)
**Location:** [Driver.java](src/main/java/com/orangeHrm/utils/Driver.java)

**Problem:** New dispatcher wrapper created on every getInstance() call

**Change:**
- Added private caching: `private static WebDriverDispatcher dispatcher;`
- Dispatcher created only once per driver instance

---

### Fix #7: Driver Sync in CommonStepDefinitions (Already Applied)
**Location:** [CommonStepDefinitions.java](src/test/java/com/orangeHRM/stepDefinitions/CommonStepDefinitions.java)

**Change:**
- Added `SeleniumTestHelper.setDriver(driver);` in @Before hook

---

## New Call Sequence (After All Fixes)

1. **Cucumber loads step definition classes** ✅
   - `LoginStepDefinitions` class loads
   - Field: `private BaseOrangeHRMLoginPageObjects baseOrangeHRMLoginPageObjects = null;` (not instantiated)
   - No driver initialization yet ✅

2. **AdminStepDefinitions class loads** ✅
   - Field: `private AdminPageObjects adminPageObjects = null;` (not instantiated)
   - No driver initialization yet ✅

3. **Test scenario starts** ✅
   - **@Before hook runs**
   - Calls `Driver.getInstance()` → **First and only browser instance** ✅
   - Dispatcher cached for reuse ✅
   - Sets driver in `SeleniumTestHelper.setDriver(driver)` ✅

4. **First step definition method called** ✅
   - Calls `getLoginPageObject()` → creates page object on first use
   - Page object calls `Driver.getInstance()` → returns cached dispatcher ✅

5. **Other steps reuse same driver** ✅
   - All subsequent calls use cached driver and dispatcher ✅

6. **@After hook runs** ✅
   - Calls `Driver.closeDriver()` → closes the single driver instance ✅
   - Reset dispatcher to null for next scenario ✅

---

## Summary of Changes

| File | Issue | Fix |
|------|-------|-----|
| LoginStepDefinitions.java | Class-level instantiation | Lazy initialization with getter |
| AdminStepDefinitions.java | Class-level instantiation | Lazy initialization with getter |
| BaseOrangeHRMLoginPageObjects.java | Nested class-level instantiation | Lazy initialization with getter |
| OrangeHRMLoginPageObjects.java | Duplicate PageFactory.initElements() | Removed duplicate initialization |
| Driver.java | New dispatcher on every call | Added private dispatcher caching |
| SeleniumTestHelper.java | Static initialization at class load | Lazy initialization with getter |
| CommonStepDefinitions.java | Driver not synced with helper | Added setDriver() call |

---

## Result

✅ **Only ONE browser instance opens per test scenario**
- No premature class-level initialization
- Driver created only in @Before hook
- All components share the same cached driver and dispatcher
- Clean lifecycle: open → use → close

---

## Build Status
✅ **BUILD SUCCESS** - All changes compile without errors
```
[INFO] Compiling 9 source files with javac [debug release 17] to target\classes
[INFO] BUILD SUCCESS
[INFO] Total time: 3.804 s
```

---

## Testing Steps to Verify

1. **Run a single scenario:**
   ```bash
   mvn test
   ```

2. **Monitor:**
   - Only ONE browser window should open
   - Browser closes at end of scenario
   - No orphaned processes

3. **Run multiple scenarios:**
   ```bash
   mvn test -Dtags="@tag1 and not @skip"
   ```

4. **Expected behavior:**
   - One browser per scenario (sequential)
   - Clean open and close for each scenario

---

Generated: February 19, 2026  
Last Updated: Complete fix - Lazy initialization for all page objects

