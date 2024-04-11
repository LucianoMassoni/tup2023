API REST en java con spring boot para la materia Laboratorio III tema 1 2023.

**Entidades:**

- Alumno
- Materia
- Profesor
- Carrera
- Asignatura
    

_**Materia**_

- Crear, Modificar y Eliminar un alumno.
    

**POST:** `/materia`  
**PUT:** `/materia/{idMateria}`  
**DELETE:** `/materia/{idMateria}`

- Obtener las materias de una misma utilizando los siguientes posibles filtros
    

**GET:**`/materia?nombre=unNombre`  
**GET:** `/materias?order=nombre_asc|nombre_desc|codigo_asc|codigo_desc`

_**Carrera**_

- Crear, Modificar y eliminar una carrera.
    

**POST:** `/carrera`  
**PUT:** `/carrera/{idCarrera}`  
**DELETE:** `/carrera/{idCarrera}`

_**Alumno**_

- Crear, Modificar y Eliminar un alumno.
    

**POST:** `/alumno`

**PUT:** `/alumno/{idAlumno}`

**DELETE:** `/alumno/{idAlumno}`

- Cursar, Aprobar o Perder regularidad de una asignatura para un alumno.  
    Para este caso, lo que ocurre es que se intentará modificar el estado de una de las asignaturas. Asumiremos cuando se crea el alumno que se creará una Lista de Asignaturas asociadas a él y la misma puede estar hardcodeada en el código.
    

**PUT:** `/alumno/{idAlumno}/asignatura/{idAsignatura}`
