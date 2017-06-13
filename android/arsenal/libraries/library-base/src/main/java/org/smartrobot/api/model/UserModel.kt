package org.smartrobot.api.model

class UserModel {
    var id: Int = 0
    var name: String? = null
    var cell_phone: String? = null
    var age: String? = null
    var address: String? = null
    var nick_name: String? = null
    var gender: Short = 0

    override fun toString(): String {
        return "UsersModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cell_phone='" + cell_phone + '\'' +
                ", age='" + age + '\'' +
                ", address='" + address + '\'' +
                ", nick_name='" + nick_name + '\'' +
                ", gender=" + gender +
                '}'
    }
}