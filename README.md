# Caso 1 - Manejo de la Concurrencia 🚀

## 📋 Descripción
Este proyecto simula una **línea de producción** para una empresa de ensamblaje, donde diferentes operarios trabajan concurrentemente para **producir, revisar y almacenar productos** utilizando **hilos (Threads) en Java**.

## 🏗️ **Arquitectura del Sistema**
El sistema sigue la siguiente estructura:

1. **Productores** 🏭
   - Generan nuevos productos o reprocesan productos rechazados.
   - Si hay productos en el *buzón de reproceso*, los procesan primero.
   - Depositan productos en el *buzón de revisión* si hay espacio disponible.

2. **Equipo de Calidad** ✅
   - Inspecciona los productos en el *buzón de revisión*.
   - Si un producto es rechazado, lo manda al *buzón de reproceso*.
   - Si es aprobado, lo envía al *depósito*.
   - Si ya se alcanzó el número de productos objetivo, genera un producto de **"FIN"** para detener la producción.

3. **Depósito** 📦
   - Almacena productos finalizados **uno a la vez** antes de su distribución.

4. **Buzón de Revisión** 📥
   - Tiene capacidad limitada.
   - Los productores deben esperar si está lleno.

5. **Buzón de Reproceso** 🔄
   - No tiene límite de capacidad.
   - Contiene productos rechazados a la espera de reprocesamiento.
---

## ⚙️ **Tecnologías y Herramientas**
- 🖥️ **Java 17+**
- 🎭 **Programación Concurrente (Threads, synchronized, wait/notify, CyclicBarrier)**
- 🏗️ **Diseño Orientado a Objetos (OOP)**
- 📜 **Diagrama UML de Clases**
- 📌 **Manejo de estados con `enum`**
---

👥 Colaboradores

Thomas Bonnett
Joban Mejia
Angelica Rodriguez

📄 Entrega
El código fuente se entrega en un archivo .zip con la siguiente estructura:

📬 Contacto
Si tienes preguntas o sugerencias, contáctanos a través de GitHub o correo electrónico.
