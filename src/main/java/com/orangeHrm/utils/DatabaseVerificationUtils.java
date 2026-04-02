package com.orangeHrm.utils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Database Verification Utility for OrangeHRM
 * Provides methods to connect to MySQL database and verify company information
 */
public class DatabaseVerificationUtils {
    private Connection connection;
    private String dbUrl;
    private String dbUsername;
    private String dbPassword;
    private String sqlFileBasePath;

    public DatabaseVerificationUtils() {
        loadDatabaseConfig();
        initializeSqlPath();
    }

    /**
     * Initialize the SQL file base path
     */
    private void initializeSqlPath() {
        String userDir = System.getProperty("user.dir");
        this.sqlFileBasePath = userDir + "/src/main/resources/sql";
    }

    /**
     * Load SQL query from file
     *
     * @param fileName SQL file name (without path)
     * @return SQL query string
     */
    private String loadSqlQuery(String fileName) {
        try {
            String filePath = sqlFileBasePath + "/" + fileName;
            String sqlContent = new String(Files.readAllBytes(Paths.get(filePath)));
            return sqlContent.trim();
        } catch (IOException e) {
            System.err.println("Failed to load SQL file: " + fileName + " - " + e.getMessage());
            throw new RuntimeException("SQL file not found: " + fileName, e);
        }
    }

    /**
     * Load database configuration from properties file
     */
    private void loadDatabaseConfig() {
        try {
            Properties properties = new Properties();
            String configPath = System.getProperty("user.dir") + "/config/configuration.properties";
            properties.load(new FileInputStream(configPath));

            this.dbUrl = properties.getProperty("db_url", "jdbc:mysql://127.0.0.1:3306/hr_mysql?useSSL=false&serverTimezone=UTC");
            this.dbUsername = properties.getProperty("db_username", "root");
            this.dbPassword = properties.getProperty("db_password", "");

            System.out.println("Database configuration loaded successfully");
            System.out.println("DB URL: " + this.dbUrl);
        } catch (IOException e) {
            System.err.println("Failed to load database configuration: " + e.getMessage());
            // Use default values
            this.dbUrl = "jdbc:mysql://127.0.0.1:3306/hr_mysql?useSSL=false&serverTimezone=UTC";
            this.dbUsername = "root";
            this.dbPassword = "";
        }
    }

    /**
     * Establish database connection
     */
    public void connect() throws SQLException, ClassNotFoundException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
            System.out.println("✓ Successfully connected to database");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found: " + e.getMessage());
            throw e;
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Close database connection
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✓ Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    /**
     * Retrieve company information by company ID
     *
     * @param companyId Company ID to search for
     * @return Map containing company information fields
     */
    public Map<String, String> getCompanyInfoById(int companyId) throws SQLException {
        Map<String, String> companyInfo = new HashMap<>();
        String query = loadSqlQuery("get_company_by_id.sql");

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                companyInfo.put("company_id", rs.getString("id"));
                companyInfo.put("company_name", rs.getString("COMPANY"));
                companyInfo.put("tax_id", rs.getString("TAX_ID"));
                companyInfo.put("phone", rs.getString("PHONE"));
                companyInfo.put("fax", rs.getString("FAX"));
                companyInfo.put("address1", rs.getString("STREET1"));
                companyInfo.put("address2", rs.getString("STREET2"));
                companyInfo.put("city", rs.getString("CITY"));
                companyInfo.put("state", rs.getString("STATE"));
                companyInfo.put("zipcode", rs.getString("ZIP"));
                companyInfo.put("country", rs.getString("COUNTRY"));
                companyInfo.put("comments", rs.getString("NOTES"));

                System.out.println("✓ Retrieved company info from DB for company ID: " + companyId);
                System.out.println("  Company Name: " + companyInfo.get("company_name"));
            } else {
                System.out.println("✗ No company found with ID: " + companyId);
            }
        }

        return companyInfo;
    }

    /**
     * Parse pipe-delimited values from geninfo_values using geninfo_keys order
     *
     * @param geninfo_keys Pipe-delimited keys string
     * @param geninfo_values Pipe-delimited values string
     * @return Map containing parsed company information
     */
    private Map<String, String> parseGeninfo(String geninfo_keys, String geninfo_values) {
        Map<String, String> companyInfo = new HashMap<>();
        
        if (geninfo_keys == null || geninfo_values == null) {
            return companyInfo;
        }
        
        String[] keys = geninfo_keys.split("\\|");
        String[] values = geninfo_values.split("\\|", -1); // -1 to keep trailing empty strings
        
        for (int i = 0; i < keys.length && i < values.length; i++) {
            String key = keys[i].trim();
            String value = values[i].trim();
            
            // Map the keys to our field names
            switch (key.toUpperCase()) {
                case "COMPANY":
                    companyInfo.put("company_name", value);
                    break;
                case "TAX":
                case "TAX_ID":
                    companyInfo.put("tax_id", value);
                    companyInfo.put("company_tax_num", value); // Also store with step definition key
                    break;
                case "PHONE":
                    companyInfo.put("phone", value);
                    break;
                case "FAX":
                    companyInfo.put("fax", value);
                    break;
                case "STREET1":
                    companyInfo.put("address1", value);
                    break;
                case "STREET2":
                    companyInfo.put("address2", value);
                    break;
                case "CITY":
                    companyInfo.put("city", value);
                    break;
                case "STATE":
                    companyInfo.put("state", value);
                    break;
                case "ZIP":
                    companyInfo.put("zipcode", value);
                    break;
                case "COUNTRY":
                    companyInfo.put("country", value);
                    break;
                case "COMMENTS":
                case "NOTES":
                    companyInfo.put("comments", value);
                    companyInfo.put("notes", value); // Also store with step definition key
                    break;
                case "NAICS":
                    companyInfo.put("naics", value);
                    break;
                default:
                    // Keep other keys as is
                    companyInfo.put(key, value);
                    break;
            }
        }
        return companyInfo;
    }

    /**
     * Retrieve company information by company name
     *
     * @param companyName Company name to search for
     * @return Map containing company information fields
     */
    public Map<String, String> getCompanyInfoByName(String companyName) throws SQLException {
        Map<String, String> companyInfo = new HashMap<>();
        String query = loadSqlQuery("get_company_by_name.sql");

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String geninfo_keys = rs.getString("geninfo_keys");
                String geninfo_values = rs.getString("geninfo_values");
                
                companyInfo = parseGeninfo(geninfo_keys, geninfo_values);
                
                System.out.println("✓ Retrieved company info from DB");
                System.out.println("  Company Name: " + companyInfo.get("company_name"));
            } else {
                System.out.println("✗ No company data found in database");
            }
        }

        return companyInfo;
    }

    /**
     * Verify if company information exists in database
     *
     * @param companyName Company name to verify
     * @return true if company exists, false otherwise
     */
    public boolean companyExists(String companyName) throws SQLException {
        String query = loadSqlQuery("check_company_exists.sql");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("company_count");
                boolean exists = count > 0;
                System.out.println(exists ? "✓ Company exists in database: " + companyName
                        : "✗ Company does not exist in database: " + companyName);
                return exists;
            }
        }
        return false;
    }

    /**
     * Get field value from parsed company info based on field name
     *
     * @param fieldName Field name to retrieve
     * @return Value from company info or null
     */
    private String getFieldValue(Map<String, String> companyInfo, String fieldName) {
        String key = fieldName.toLowerCase();
        return companyInfo.get(key);
    }

    /**
     * Verify a specific company field value in database
     *
     * @param companyName Company name to search for (context only)
     * @param fieldName Field name to verify
     * @param expectedValue Expected value
     * @return true if field value matches expected value
     */
    public boolean verifyCompanyField(String companyName, String fieldName, String expectedValue) throws SQLException {
        if (expectedValue == null || expectedValue.trim().isEmpty()) {
            System.out.println("⊘ Skipping verification for " + fieldName + " (expected value is null/empty)");
            return true;
        }

        String query = loadSqlQuery("get_company_by_name.sql");
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String geninfo_keys = rs.getString("geninfo_keys");
                String geninfo_values = rs.getString("geninfo_values");
                
                Map<String, String> companyInfo = parseGeninfo(geninfo_keys, geninfo_values);
                String actualValue = getFieldValue(companyInfo, fieldName);
                
                if (actualValue != null) {
                    boolean matches = actualValue.trim().equals(expectedValue.trim());

                    if (matches) {
                        System.out.println("✓ Field '" + fieldName + "' verified: " + actualValue);
                    } else {
                        System.out.println("✗ Field '" + fieldName + "' mismatch!");
                        System.out.println("  Expected: " + expectedValue);
                        System.out.println("  Actual:   " + actualValue);
                    }
                    return matches;
                } else {
                    System.out.println("✗ Field '" + fieldName + "' not found in company info");
                    return false;
                }
            } else {
                System.out.println("✗ No company data found in database");
                return false;
            }
        }
    }

    /**
     * Verify all company fields against provided map
     *
     * @param companyName Company name to search for
     * @param expectedFields Map of field names and expected values
     * @return Map of field names and verification results (true/false)
     */
    public Map<String, Boolean> verifyCompanyFields(String companyName, Map<String, String> expectedFields) throws SQLException {
        Map<String, Boolean> results = new HashMap<>();

        for (Map.Entry<String, String> entry : expectedFields.entrySet()) {
            String fieldName = entry.getKey();
            String expectedValue = entry.getValue();
            boolean result = verifyCompanyField(companyName, fieldName, expectedValue);
            results.put(fieldName, result);
        }

        // Summary
        long passedCount = results.values().stream().filter(v -> v).count();
        long totalCount = results.size();
        System.out.println("\n📋 Database Verification Summary: " + passedCount + "/" + totalCount + " fields verified");

        return results;
    }

    /**
     * Get country name from country code
     *
     * @param countryCode Country code
     * @return Country name
     */
    public String getCountryNameByCode(String countryCode) throws SQLException {
        String query = "SELECT name FROM osh_country WHERE country_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, countryCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    /**
     * Get state/province name from state code
     *
     * @param countryCode Country code
     * @param stateCode State/province code
     * @return State/province name
     */
    public String getStateNameByCode(String countryCode, String stateCode) throws SQLException {
        String query = "SELECT name FROM osh_state WHERE country_code = ? AND province_code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, countryCode);
            stmt.setString(2, stateCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    /**
     * Check if connection is active
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Get all company names from database
     *
     * @return List of company names
     */
    public java.util.List<String> getAllCompanyNames() throws SQLException {
        java.util.List<String> companies = new java.util.ArrayList<>();
        String query = "SELECT company_name FROM osh_company ORDER BY company_name";

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                companies.add(rs.getString("company_name"));
            }
        }

        System.out.println("✓ Retrieved " + companies.size() + " companies from database");
        return companies;
    }
}
