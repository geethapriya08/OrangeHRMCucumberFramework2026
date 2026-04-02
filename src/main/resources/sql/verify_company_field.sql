-- Verify a specific field value for a company by name
SELECT ? as field_name, {field_name} as actual_value 
FROM `hs_hr_geninfo` 
WHERE `name` = ?;
