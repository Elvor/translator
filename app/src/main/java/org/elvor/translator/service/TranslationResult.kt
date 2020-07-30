package org.elvor.translator.service

data class TranslationResult(val options: List<TranslationResultOption>)

data class TranslationResultOption(val name: String, val items: List<TranslationResultItem>)

data class TranslationResultItem(val subject: String?, val value: String)