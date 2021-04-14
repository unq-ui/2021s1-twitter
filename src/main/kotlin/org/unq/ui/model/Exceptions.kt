package org.unq.ui.model

class UsedEmail : Exception("Email used")
class NotFound(msg: String) : Exception("Not found $msg")
class NotATag : Exception("Missing #")

