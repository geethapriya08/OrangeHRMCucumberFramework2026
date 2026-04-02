-- Get company by tax ID from hs_hr_geninfo table
SELECT * 
FROM `hs_hr_geninfo` 
WHERE `tax_id` = ?;
