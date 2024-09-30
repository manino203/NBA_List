package com.example.nba_list.data.network

class HttpErrorException(val code: Int, val body: String?): Exception()