package com.example.domain

data class Photo (
    var id: Int,
    var sol: Int,
    var camera: Camera,
    var img_src: String,
    var earth_date: String,
    var rover: Rover
)

