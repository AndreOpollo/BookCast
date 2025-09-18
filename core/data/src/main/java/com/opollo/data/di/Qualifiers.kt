package com.opollo.data.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class JsonRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class XmlRetrofit