package com.cevaris.dedup.exceptions

sealed trait DedupError extends  RuntimeException

object MissingSourceFile extends DedupError