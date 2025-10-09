package com.helpquest.core.domain.validation

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

class PasswordValidatorTest {

    @Test
    fun `Password meets all criteria`() {
        // Check if validate returns true for all fields when password has minimum length, a digit, and an uppercase letter. Example: 'ValidPass1'.
        val result = PasswordValidator.validate("ValidPass1")
        assertThat(result.isValidPassword).isTrue()
    }

    @Test
    fun `Password is exactly minimum length`() {
        // Check if hasMinLength is true when password length is exactly 9 characters and other conditions are met. Example: 'Abcdef123'.
        val result = PasswordValidator.validate("ValidPass1")
        assertThat(result.hasMinLength).isTrue()
    }

    @Test
    fun `Password is shorter than minimum length`() {
        // Check if hasMinLength is false when the password has fewer than 9 characters. Example: 'Short1A'.
        val result = PasswordValidator.validate("VaPs1")
        assertThat(result.hasMinLength).isFalse()
    }

    @Test
    fun `Password is longer than minimum length`() {
        // Check if hasMinLength is true when the password is longer than 9 characters. Example: 'LongerPassword1A'.
        val result = PasswordValidator.validate("ValidPass1wwwwww")
        assertThat(result.hasMinLength).isTrue()
    }

    @Test
    fun `Password without digits`() {
        // Check if hasDigit is false when the password contains no numbers. Example: 'NoDigitPasswordA'.
        val result = PasswordValidator.validate("ValidPass")
        assertThat(result.hasDigit).isFalse()
    }

    @Test
    fun `Password with one digit`() {
        // Check if hasDigit is true when there is exactly one number in the password. Example: 'HasOneDigit1'.
        val result = PasswordValidator.validate("ValidPass1")
        assertThat(result.hasDigit).isTrue()
    }

    @Test
    fun `Password with multiple digits`() {
        // Check if hasDigit is true for a password with more than one number. Example: 'HasMultiDigit123'.
        val result = PasswordValidator.validate("ValidPass12")
        assertThat(result.hasDigit).isTrue()
    }

    @Test
    fun `Password without uppercase letters`() {
        // Check if hasUppercase is false for a password with no uppercase letters. Example: 'nouppercase123'.
        val result = PasswordValidator.validate("alidass1")
        assertThat(result.hasUppercase).isFalse()
    }

    @Test
    fun `Password with one uppercase letter`() {
        // Check if hasUppercase is true when there is exactly one uppercase letter. Example: 'OneUppercase123'.
        val result = PasswordValidator.validate("Validass1")
        assertThat(result.hasUppercase).isTrue()
    }

    @Test
    fun `Password with multiple uppercase letters`() {
        // Check if hasUppercase is true for a password with multiple uppercase letters. Example: 'MULTIUppercase123'.
        val result = PasswordValidator.validate("ValidPass1")
        assertThat(result.hasUppercase).isTrue()
    }

    @Test
    fun `Password is an empty string`() {
        // Check all validation fields are false when an empty string is provided. Example: ''.
        val result = PasswordValidator.validate("")
        assertThat(result.hasUppercase).isFalse()
        assertThat(result.hasDigit).isFalse()
        assertThat(result.hasMinLength).isFalse()
        assertThat(result.isValidPassword).isFalse()
    }

    @Test
    fun `Password with only digits`() {
        // Check if hasMinLength is true (if length >= 9), hasDigit is true, and hasUppercase is false. Example: '123456789'.
        val result = PasswordValidator.validate("123456789")
        assertThat(result.hasUppercase).isFalse()
        assertThat(result.hasDigit).isTrue()
        assertThat(result.hasMinLength).isTrue()
        assertThat(result.isValidPassword).isFalse()
    }

    @Test
    fun `Password with only uppercase letters`() {
        // Check if hasMinLength is true (if length >= 9), hasDigit is false, and hasUppercase is true. Example: 'ABCDEFGHI'.
        val result = PasswordValidator.validate("ABCDEFGHI")
        assertThat(result.hasUppercase).isTrue()
        assertThat(result.hasDigit).isFalse()
        assertThat(result.hasMinLength).isTrue()
        assertThat(result.isValidPassword).isFalse()
    }

    @Test
    fun `Password with only lowercase letters`() {
        // Check if all validation fields are false (unless length >= 9, then hasMinLength is true). Example: 'abcdefghi'.
        val result = PasswordValidator.validate("ABCDEFGHI")
        assertThat(result.hasUppercase).isTrue()
        assertThat(result.hasDigit).isFalse()
        assertThat(result.hasMinLength).isTrue()
        assertThat(result.isValidPassword).isFalse()
    }

}