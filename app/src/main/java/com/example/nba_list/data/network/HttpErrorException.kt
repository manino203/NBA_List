package com.example.nba_list.data.network

/**
 * Exception thrown when an HTTP error occurs during a network request.
 *
 * @param code The HTTP status code returned from the API.
 * @param body The response body as a string, or null if not available.
 */
class HttpErrorException(val code: Int, val body: String?): Exception()