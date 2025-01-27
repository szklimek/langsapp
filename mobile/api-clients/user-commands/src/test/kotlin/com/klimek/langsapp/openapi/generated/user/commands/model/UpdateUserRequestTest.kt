/**
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 *
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package com.klimek.langsapp.openapi.generated.user.commands.model

import io.kotlintest.shouldBe
import io.kotlintest.specs.ShouldSpec

import com.klimek.langsapp.openapi.generated.user.commands.model.UpdateUserRequest
import com.klimek.langsapp.openapi.generated.user.commands.model.LanguageSettings

class UpdateUserRequestTest : ShouldSpec() {
    init {
        // uncomment below to create an instance of UpdateUserRequest
        //val modelInstance = UpdateUserRequest()

        // to test the property `username` - Chosen username
        should("test username") {
            // uncomment below to test the property
            //modelInstance.username shouldBe ("TODO")
        }

        // to test the property `avatarUrl` - URL of previously uploaded image of avatar
        should("test avatarUrl") {
            // uncomment below to test the property
            //modelInstance.avatarUrl shouldBe ("TODO")
        }

        // to test the property `languageSettings`
        should("test languageSettings") {
            // uncomment below to test the property
            //modelInstance.languageSettings shouldBe ("TODO")
        }

        // to test the property `propertiesToDelete` - Array of fields to clear. Note that some of them may not be allowed to be removed
        should("test propertiesToDelete") {
            // uncomment below to test the property
            //modelInstance.propertiesToDelete shouldBe ("TODO")
        }

    }
}
