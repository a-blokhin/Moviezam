package com.example.moviezam.models

data class Resource<T>(
    val status: Status,
    val data: T?,
    val message: String? = null
) {
    enum class Status {
        SUCCESS, ERROR, LOADING
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(
                Status.SUCCESS,
                data,
                "OK"
            )
        }

        fun <T> error(data: T?): Resource<T> {
            return Resource(
                Status.ERROR,
                data,
                "Server error"
            )
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(
                Status.LOADING,
                data,
                "Loading"
            )
        }
    }
}