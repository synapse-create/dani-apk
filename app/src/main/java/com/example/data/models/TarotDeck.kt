package com.example.data.models

import kotlin.random.Random

data class TarotCard(
    val id: Int,
    val name: String,
    val isUpright: Boolean = true,
    val meaningUpright: String,
    val meaningReversed: String,
    val element: String,
    val description: String,
    val advice: String,
    val focusArea: String // "Amor", "Salud", "Dinero", "Espiritualidad"
)

object TarotDeck {
    val cards = listOf(
        TarotCard(
            0, "El Loco", true,
            "Nuevos comienzos, espontaneidad, fe, salto de fe, inocencia pura.",
            "Imprudencia, riesgos innecesarios, ingenuidad, desconexión del peligro.",
            "Aire",
            "Un joven camina hacia el borde de un acantilado con una rosa blanca en la mano y un perrito a su lado.",
            "Confía en el universo, pero fíjate bien por dónde caminas. Abre las alas con libertad y sin prejuicios.",
            "Espiritualidad"
        ),
        TarotCard(
            1, "El Mago", true,
            "Poder personal, iniciativa, manifestación, concentración, habilidad y voluntad.",
            "Manipulación, potencial sin usar, planes mal encauzados, ilusiones falsas.",
            "Aire",
            "Una figura de pie ante un altar con los cuatro elementos del Tarot (copa, espada, basto, pentáculo) extendidos ante sí.",
            "Tienes todas las herramientas necesarias para manifestar tus deseos en el plano físico. Confía en tu talento.",
            "Dinero"
        ),
        TarotCard(
            2, "La Sacerdotisa", true,
            "Intuición inconsciente, sabiduría divina, misterio, voz interior.",
            "Secretos revelados, intuición ignorada, superficialidad, miedo a la verdad interna.",
            "Agua",
            "Sentada silenciosa entre una columna blanca y una negra, sosteniendo un pergamino de sabiduría ancestral.",
            "No busques respuestas en el exterior. Entra en el silencio sagrado de tu alma y escucha tu voz interior.",
            "Espiritualidad"
        ),
        TarotCard(
            3, "La Emperatriz", true,
            "Abundancia, fertilidad, naturaleza creadora, amor incondicional, sensualidad.",
            "Bloqueo creativo, dependencia, asfixia emocional, escasez percibida.",
            "Tierra",
            "Una dama coronada de estrellas sentada confortablemente en un trono dorado rodeado de frondosos campos de trigo.",
            "Conéctate con la abundancia de la Madre Tierra. Nutre tus proyectos con amor sagrado y déjalos florecer.",
            "Amor"
        ),
        TarotCard(
            4, "El Emperador", true,
            "Estructura, autoridad, estabilidad, protección paterna, disciplina.",
            "Tiranía, inflexibilidad, falta de control, desorganización total.",
            "Fuego",
            "Un soberano maduro sentado en un sólido trono de piedra decorado con cabezas de carnero.",
            "Establece límites sanos y organiza tu vida diaria. La disciplina constructiva protege tus sueños.",
            "Salud"
        ),
        TarotCard(
            5, "El Sumo Sacerdote", true,
            "Conexión espiritual, tradición, valores éticos, mentoría, sabiduría espiritual.",
            "Rigidez dogmática, rebeldía contra lo establecido, consejo equivocado.",
            "Tierra",
            "Un líder espiritual dando su bendición con dos dedos alzados ante sus fieles devotos en un templo sagrado.",
            "Alinea tus acciones con tus valores más profundos. Busca guías o libros que iluminen tu actual sendero.",
            "Espiritualidad"
        ),
        TarotCard(
            6, "Los Enamorados", true,
            "Elecciones del corazón, alineación de valores, amor sagrado, relaciones armónicas.",
            "Desarmonía, decisiones difíciles postergadas, desconexión interna.",
            "Aire",
            "Una pareja bendecida por un gran ángel en el cielo resplandeciente, rodeados del árbol de la vida.",
            "Toma decisiones con el corazón y el alma, no solo con la cabeza. Elige la autenticidad en el amor.",
            "Amor"
        ),
        TarotCard(
            7, "El Carro", true,
            "Determinación, victoria, control de fuerzas opuestas, fuerza de voluntad.",
            "Falta de dirección, pérdida de control, agresividad destructiva o parálisis.",
            "Agua",
            "Un auriga coronado guiando un carro tirado por dos esfinges, una blanca y otra negra, en armonía.",
            "Sincroniza tus impulsos opuestos (mente y emoción) y dirígelos hacia tus metas con paso firme.",
            "Dinero"
        ),
        TarotCard(
            8, "La Fuerza", true,
            "Valentía interior, compasión, domar las pasiones, paciencia espiritual.",
            "Debilidad, inseguridad, ira desatada, abuso de poder sobre otros.",
            "Fuego",
            "Una mujer coronada de guirnaldas florales que cierra con infinita suavidad y compasión la boca de un león.",
            "Tu poder reside en la suavidad y el autocompromiso. Domina tus impulsos con amor e indulgencia divina.",
            "Salud"
        ),
        TarotCard(
            9, "El Ermitaño", true,
            "Introspección, soledad espiritual, búsqueda de luz, sabiduría interna.",
            "Aislamiento excesivo, reclusión triste, testarudez, rechazo al aprendizaje.",
            "Tierra",
            "Un anciano en la cima de una gran montaña nevada, sosteniendo un farol dorado con una estrella de seis puntas.",
            "Es momento de retirarte un poco del ruido exterior para reflexionar. Tu luz interna iluminará la siguiente decisión.",
            "Espiritualidad"
        ),
        TarotCard(
            10, "La Rueda de la Fortuna", true,
            "Destino, cambio de ciclo, sincronicidades cósmicas, karma en movimiento.",
            "Mala suerte aparente, resistencia al flujo natural, interrupción de ciclos.",
            "Fuego",
            "Una gran rueda mística suspendida en las nubes, habitada por figuras celestiales en rotación eterna.",
            "Comprende que el cambio es la única constante celeste. Todo pasa, fluye con los altibajos sin aferrarte.",
            "Dinero"
        ),
        TarotCard(
            11, "La Justicia", true,
            "Verdad, causa y efecto, equidad, equilibrio kármico, honestidad radical.",
            "Injusticia, deshonestidad, prejuicios, desprecio por las consecuencias.",
            "Aire",
            "Una figura majestuosa entronizada que sostiene una balanza dorada perfectamente equilibrada en una mano y una espada alzada.",
            "Sé honesto contigo mismo y con el mundo. Toda semilla que siembras hoy dará frutos en el mañana.",
            "Dinero"
        ),
        TarotCard(
            12, "El Colgado", true,
            "Nueva perspectiva, letargo voluntario, renuncia, entrega al destino divino.",
            "Esfuerzo inútil, victimismo, procrastinación, resistencia a soltar.",
            "Agua",
            "Un joven suspendido de un pie en un árbol en forma de cruz, rodeado de un aura dorada resplandeciente en la cabeza.",
            "No trates de forzar las situaciones. ríndete al momento presente y mira las cosas desde otro punto de visión.",
            "Espiritualidad"
        ),
        TarotCard(
            13, "La Muerte", true,
            "Transformación profunda, finales necesarios, transmutación, renacimiento.",
            "Resistencia al cambio, estancamiento doloroso, miedo obsesivo a soltar.",
            "Agua",
            "Un jinete esquelético con armadura negra cruza un campo donde sale el sol, invitando a la renovación total.",
            "Deja morir lo que ya no sirve a tu evolución espiritual. De las cenizas del pasado nacerá una luz sagrada.",
            "Salud"
        ),
        TarotCard(
            14, "La Templanza", true,
            "Equilibrio, sanación, moderación, paciencia cósmica, alquimia espiritual.",
            "Desequilibrio, excesos autoestimulantes, discordia, impaciencia destructora.",
            "Fuego",
            "Un ángel majestuoso traspasando agua luminosa con destreza entre dos copas doradas sin que se derrame.",
            "Integra tus opuestos. Sánate a través de la paciencia, la paz interna y sabias dosis en cada área de tu vida.",
            "Salud"
        ),
        TarotCard(
            15, "El Diablo", true,
            "Lazos materiales, tentación terrenal, pasiones inconscientes, romper tabúes.",
            "Liberación de apegos, despertar espiritual, superar adicciones y miedos.",
            "Fuego",
            "Un ser con cuernos sentado sobre un pedestal, al cual se encuentran encadenados dos amantes con cadenas muy holgadas.",
            "Detecta de qué dependencias psicológicas o prejuicios te estás encadenando. La llave de tu libertad está en tu bolsillo.",
            "Amor"
        ),
        TarotCard(
            16, "La Torre", true,
            "Revelación repentina, ruptura de falsas bases, liberación abrupta, despertar.",
            "Catástrofe evitada o retrasada, aferramiento a lo falso, miedo de caer.",
            "Fuego",
            "Un rayo celestial golpea la parte alta de una torre construida sobre roca sólida, desmoronando las coronas.",
            "Agradece que se caigan las estanterías de falsas certezas. El rayo purificador despeja el espacio para la verdad pura.",
            "Salud"
        ),
        TarotCard(
            17, "La Estrella", true,
            "Esperanza cósmica, inspiración divina, fe renovada, serenidad del alma, brillo solar.",
            "Falta de fe, desánimo, pesimismo ciego, desconexión de la guía divina.",
            "Aire",
            "Una bella joven desnuda derrama agua medicinal en la tierra fértil y en un estanque bajo un firmamento estrellado.",
            "Estás siendo guiado y protegido por las estrellas de la providencia. Tu herida se está sanando con amor suave.",
            "Amor"
        ),
        TarotCard(
            18, "La Luna", true,
            "Misterios del inconsciente, intuición psíquica, sueños sagrados, ilusión, temores.",
            "Miedos desatados liberados, confusión superada, autodecepción develada.",
            "Agua",
            "Dos perros aúllan al místico disco lunar que tiene un rostro, mientras un cangrejo surge de un oscuro estanque profundo.",
            "Cruza el valle de tus miedos inconscientes. Tus instintos y sueños traen mensajes sagrados que debes descifrar hoy.",
            "Espiritualidad"
        ),
        TarotCard(
            19, "El Sol", true,
            "Claridad pura, vitalidad radiante, éxito dichoso, felicidad del niño interior.",
            "Falta de claridad, egocentrismo cegador, orgullo, deprimida energía temporal.",
            "Fuego",
            "Un niño sonriente monta un majestuoso corcel blanco bajo un inmenso sol brillante, rodeado de alegres girasoles.",
            "Brilla con toda tu luz divina. Reclama la alegría de vivir, el juego y agradece todo el calor divino en tu camino.",
            "Salud"
        ),
        TarotCard(
            20, "El Juicio", true,
            "Despertar espiritual, redención de culpas, vocación cósmica, tomar conciencia.",
            "Duda de ti mismo, remordimientos inútiles, llamada espiritual rechazada.",
            "Fuego",
            "Un ángel resuena una trompeta celestial en el firmamento y las almas despiertan de su letargo abriendo los brazos.",
            "Escucha tu llamada sagrada del alma. Perdona tu pasado y levántate para reclamar tu nuevo nivel de existencia.",
            "Espiritualidad"
        ),
        TarotCard(
            21, "El Mundo", true,
            "Realización plena, integración de ciclos, armonía universal, éxito rotundo.",
            "Ciclo sin cerrar, éxito retrasado, estancamiento final por pereza.",
            "Tierra",
            "Una danzarina celestial envuelta en un sudario de sabiduría flota dentro de una gran guirnalda de laurel victorioso.",
            "Disfruta del éxito de tus ciclos completados. Siente la profunda interconexión de todo el cosmos contigo. Eres uno.",
            "Espiritualidad"
        )
    )

    fun drawCards(count: Int): List<TarotCard> {
        val shuffled = cards.shuffled()
        return shuffled.take(count).map { card ->
            card.copy(isUpright = Random.nextBoolean())
        }
    }
}
