SELECT
    Student.name AS student_name,
    Student.age AS student_age,
    Faculty.name AS faculty_name
FROM
    Student
JOIN
    Faculty ON Student.faculty_id = Faculty.id
WHERE
    Faculty.name = 'Хогвартс';

SELECT
    Student.name AS student_name,
    Student.age AS student_age,
    Faculty.name AS faculty_name
FROM
    Student
JOIN
    Faculty ON Student.faculty_id = Faculty.id
WHERE
    Student.avatar IS NOT NULL;
