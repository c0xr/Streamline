package com.cory.streamline.register

data class RegisterFromState (
    var passwordError:Int?=null,
    var passwordMatchError:Int?=null,
    var usernameError:Int?=null,
    var nicknameError:Int?=null,
    var isDataValid:Boolean=false
)