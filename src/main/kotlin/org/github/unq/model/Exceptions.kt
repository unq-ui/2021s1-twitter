package org.github.unq.model

class UsedEmail : Exception("Email used")
class NotFound(msg: String) : Exception("Not found $msg")
class NotATag : Exception("Missing #")

