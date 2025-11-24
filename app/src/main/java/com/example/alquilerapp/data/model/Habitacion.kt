package com.example.alquilerapp.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Representa los datos de una habitación disponible para alquilar.
 *
 * Esta clase es serializable para poder ser transferida, por ejemplo, en formato JSON
 * desde una API. Contiene toda la información relevante de una habitación.
 *
 * @property id El identificador único de la habitación.
 * @property titulo El nombre o título principal del anuncio de la habitación.
 * @property ciudad La ciudad donde se encuentra la habitación.
 * @property direccion La dirección específica de la propiedad.
 * @property precioMensual El coste del alquiler por mes. La anotación `@SerialName` se usa para mapear este campo con el nombre "precioMensual" en el JSON.
 * @property descripcion Un texto detallado con las características y condiciones del alquiler.
 * @property imagenesUrl Una lista de URLs (en formato String) que apuntan a las imágenes de la habitación.
 */
@Serializable
data class Habitacion(
    val id: String,
    val titulo: String,
    val ciudad: String,
    val direccion: String,
    @SerialName("precioMensual")
    val precioMensual: Double,
    val descripcion: String,
    val imagenesUrl: List<String>,
)

/**
* Obtiene la URL de la primera imagen de la habitación, adaptada para el emulador de Android.
*
* Esta función de extensión toma la primera URL de la lista `imagenesUrl` y reemplaza
* "localhost" con "10.0.2.2". Esto es necesario para que el emulador de Android pueda
* acceder a un servidor que se está ejecutando en la máquina anfitriona (host).
*
* @return La URL modificada como un [String], o `null` si la lista de imágenes está vacía.
*/
fun Habitacion.getEmulatedImageUrl(): String? {
    return this.imagenesUrl.firstOrNull()?.replace("localhost", "10.0.2.2")
}
