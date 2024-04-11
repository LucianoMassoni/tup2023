API REST en java con spring boot para la materia Laboratorio III tema 1 2023.
<hr/>

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

<hr/>

**Documentación en postman:**

[Postman](https://documenter.getpostman.com/view/29939805/2sA3BheEpq)

<hr/>

**Diagramas UML:**

![final](https://github.com/LucianoMassoni/tup2023/assets/112901637/e0d72713-82da-4cfc-bff1-b2e28c8c6c71)
- [model](https://github.com/LucianoMassoni/tup2023/assets/112901637/fb1ed245-8474-4a2a-a2f5-fc0579e77f14)
- [model_dto](https://github.com/LucianoMassoni/tup2023/assets/112901637/d873a277-5ad2-40d8-a437-31df9db2b8e2)
- [model_exception](https://github.com/LucianoMassoni/tup2023/assets/112901637/fa754675-3f95-46e7-959a-07a558236014)
- [controller](https://github.com/LucianoMassoni/tup2023/assets/112901637/38ff7830-b280-4c21-9b8e-680f90abf930)
- [controller_handler](https://github.com/LucianoMassoni/tup2023/assets/112901637/5ed61103-1372-49b5-9f4b-948db6bb52b1)
- [business](https://github.com/LucianoMassoni/tup2023/assets/112901637/87a68452-632d-4d1e-a8ba-5b590c2b2bc8)
- [business_impl](https://github.com/LucianoMassoni/tup2023/assets/112901637/35ea7c78-448e-43a6-8106-dc13f7704aa7)
- [persistence](https://github.com/LucianoMassoni/tup2023/assets/112901637/2ef9019e-afcb-467f-bfd5-2f58d2192104)
- [persistence_impl](https://github.com/LucianoMassoni/tup2023/assets/112901637/c9ed8b71-d956-4be9-a5c4-02b7ed6d2563)
- [persistence_exception](https://github.com/LucianoMassoni/tup2023/assets/112901637/a4ce1f5e-5f8d-4f16-90b7-bf48bb6fc02f)


