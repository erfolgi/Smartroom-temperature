package com.example.sensorsuhu.model

data class Response(
	val History: History?=null,
	val Kipas: Kipas?=null,
	val Lampu: Lampu?=null,
	val Suhu: Suhu?=null

)