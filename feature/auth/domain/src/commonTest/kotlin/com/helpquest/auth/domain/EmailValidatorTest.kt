package com.helpquest.auth.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

class EmailValidatorTest {

    @Test
    fun `Valid standard email`() {
        // Test with a standard and commonly used email format.
        assertThat(EmailValidator.validate("test@test.com")).isTrue()
    }

    @Test
    fun `Valid email with subdomain`() {
        // Test an email that includes a subdomain in the domain part. [19]
        assertThat(EmailValidator.validate("test@test.test.com")).isTrue()

    }

    @Test
    fun `Invalid email missing @ symbol`() {
        // Test an email string that completely lacks the '@' symbol. [4]
        assertThat(EmailValidator.validate("testtest.com")).isFalse()

    }

    @Test
    fun `Invalid email with multiple  @ symbols`() {
        // Test an email containing more than one '@' symbol. [2]
        assertThat(EmailValidator.validate("test@@test.com")).isFalse()

    }

    @Test
    fun `Invalid email with no local part`() {
        // Test an email that starts with the '@' symbol, having no local part. [8]
        assertThat(EmailValidator.validate("@test.com")).isFalse()

    }

    @Test
    fun `Invalid email with no domain part`() {
        // Test an email that ends after the '@' symbol, missing the domain. [2]
        assertThat(EmailValidator.validate("test@.com")).isFalse()

    }

    @Test
    fun `Invalid email with no top level domain`() {
        // Test an email where the domain has no TLD (e.g., 'user@domain'). [2]
        assertThat(EmailValidator.validate("test@test")).isFalse()

    }

    @Test
    fun `Invalid email with TLD less than two characters`() {
        // Test an email where the TLD is only one character long. [2]
        assertThat(EmailValidator.validate("test@test.c")).isFalse()

    }

    @Test
    fun `Invalid email with space in local part`() {
        // Test an email containing a space within the local part. [2]
        assertThat(EmailValidator.validate("t est@test.com")).isFalse()

    }

    @Test
    fun `Invalid email with space in domain part`() {
        // Test an email containing a space within the domain name.
        assertThat(EmailValidator.validate("test@te st.com")).isFalse()

    }

    @Test
    fun `Invalid characters in local part`() {
        // Test an email with invalid special characters (e.g., '#', '$', '!') in the local part not covered by the regex.
        assertThat(EmailValidator.validate("t$!st@test.com")).isFalse()

    }

    @Test
    fun `Invalid characters in domain part`() {
        // Test an email with invalid characters (e.g., '_', '+', '!') in the domain part.
        assertThat(EmailValidator.validate("test@te!st.com")).isFalse()

    }

    @Test
    fun `Empty string input`() {
        // Test the function with an empty string to check for robustness. [10]
        assertThat(EmailValidator.validate("")).isFalse()

    }

    @Test
    fun `Email with only an  @ symbol`() {
        // Test an email that consists solely of the '@' symbol. [10]
        assertThat(EmailValidator.validate("@")).isFalse()

    }


    @Test
    fun `Email ending with a dot`() {
        // Test an email string that has a trailing dot at the very end.
        assertThat(EmailValidator.validate("test@test.com.")).isFalse()

    }

}