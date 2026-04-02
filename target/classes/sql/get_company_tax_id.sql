-- Get company tax ID from hs_hr_geninfo table
SELECT `tax_id` 
FROM `hs_hr_geninfo` 
WHERE `name` = ?;
