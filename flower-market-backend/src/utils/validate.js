/**
 * Form validation rules
 */

// Phone number validation
export const validatePhone = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please enter your phone number'))
  } else if (!/^1[3-9]\d{9}$/.test(value)) {
    callback(new Error('Please enter a valid phone number'))
  } else {
    callback()
  }
}

// Email validation (optional)
export const validateEmail = (rule, value, callback) => {
  if (!value) {
    callback() // allowed to be empty
  } else if (!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)) {
    callback(new Error('Please enter a valid email address'))
  } else {
    callback()
  }
}

// Email validation (required)
export const validateEmailRequired = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please enter your email address'))
  } else if (!/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(value)) {
    callback(new Error('Please enter a valid email address'))
  } else {
    callback()
  }
}

// Password validation (4-20 chars)
export const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please enter your password'))
  } else if (value.length < 4 || value.length > 20) {
    callback(new Error('Password length must be 4-20 characters'))
  } else {
    callback()
  }
}

// Username validation (4-20 letters, numbers, underscore)
export const validateUsername = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please enter a username'))
  } else if (value.length < 4 || value.length > 20) {
    callback(new Error('Username length must be 4-20 characters'))
  } else if (!/^[a-zA-Z0-9_]{4,20}$/.test(value)) {
    callback(new Error('Username can only contain letters, numbers, and underscores'))
  } else {
    callback()
  }
}

// Chinese name validation
export const validateChineseName = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please enter your name'))
  } else if (value.length < 2 || value.length > 10) {
    callback(new Error('Name length must be 2-10 characters'))
  } else {
    callback()
  }
}

// ID card validation
export const validateIdCard = (rule, value, callback) => {
  if (!value) {
    callback(new Error('Please enter your ID number'))
  } else if (!/^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/.test(value)) {
    callback(new Error('Please enter a valid ID number'))
  } else {
    callback()
  }
}

// URL validation
export const validateURL = (rule, value, callback) => {
  if (!value) {
    callback()
  } else if (!/^https?:\/\/.+/.test(value)) {
    callback(new Error('Please enter a valid URL'))
  } else {
    callback()
  }
}

// Price validation
export const validatePrice = (rule, value, callback) => {
  if (!value && value !== 0) {
    callback(new Error('Please enter a price'))
  } else if (!/^(0|[1-9]\d*)(\.\d{1,2})?$/.test(value)) {
    callback(new Error('Please enter a valid price (up to 2 decimal places)'))
  } else if (Number(value) < 0) {
    callback(new Error('Price cannot be negative'))
  } else {
    callback()
  }
}

// Quantity validation
export const validateQuantity = (rule, value, callback) => {
  if (!value && value !== 0) {
    callback(new Error('Please enter a quantity'))
  } else if (!/^[1-9]\d*$/.test(value)) {
    callback(new Error('Please enter a positive integer'))
  } else {
    callback()
  }
}
