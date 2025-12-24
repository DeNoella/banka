import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import AuthService from '../auth/AuthService'
import { toast } from "react-toastify"
import { useTheme } from '../context/ThemeContext'



const Login = () => {
    const navigate = useNavigate()
    const { darkMode, toggleDarkMode } = useTheme()
    const [isLoginMode, setIsLoginMode] = useState(true)
    const [otp, setOtp] = useState(['', '', '', '', '', ''])
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [otpSent, setOtpSent] = useState(false)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const [success, setSuccess] = useState('')

    const handleOtpChange = (value, index) => {
        if (!/^\d*$/.test(value)) return
        const newOtp = [...otp]
        newOtp[index] = value
        setOtp(newOtp)

        if (value && index < 5) {
            const nextInput = document.getElementById(`otp-${index + 1}`)
            nextInput?.focus()
        }
    }

    const handleRequestOtp = async (e) => {
        e.preventDefault()
        setLoading(true)
        setError('')
        setSuccess('')

        try {
            const response = await AuthService.requestOtp(email)
            toast.success(response.data.message || 'OTP sent to your email!')
            setOtpSent(true)
        } catch (err) {
            console.error("Login Error:", err);
            const msg = err.response?.data?.message || err.message || 'Failed to send OTP.'
            toast.error(msg)
        } finally {
            setLoading(false)
        }
    }

    const handleVerifyOtp = async (e) => {
        e.preventDefault()
        setLoading(true)
        setError('')

        const otpString = otp.join('')

        if (otpString.length !== 6) {
            toast.error('Please enter all 6 digits')
            setLoading(false)
            return
        }

        try {
            const response = await AuthService.verifyOtp(email, otpString)

            // DEBUG: Log the response
            console.log('Full Response:', response)
            console.log('Response Data:', response.data)
            console.log('User Object:', response.data.user)
            console.log('Role from backend:', response.data.user?.userRole)

            // Store token and user data
            // Handle both 'role' and 'user_Role' field names
            const userRole = response.data.user.role || response.data.user.userRole

            localStorage.setItem('token', response.data.token)
            localStorage.setItem('role', userRole)
            localStorage.setItem('userId', response.data.user.userId)
            localStorage.setItem('email', response.data.user.email)
            localStorage.setItem('firstName', response.data.user.firstName)
            localStorage.setItem('lastName', response.data.user.lastName)

            // DEBUG: Check what was stored
            console.log('Stored Role:', localStorage.getItem('role'))

            // Redirect based on role
            const role = response.data.user.role || response.data.user.userRole
            console.log('Redirecting based on role:', userRole)

            // Handle both "ADMIN" and "ROLE_ADMIN" formats
            const cleanRole = role.replace('ROLE_', '')

            switch (cleanRole) {
                case 'ADMIN':
                    console.log('Navigating to /admin-dashboard')
                    navigate('/admin-dashboard', { replace: true })
                    break
                case 'MANAGER':
                    console.log('Navigating to /manager-dashboard')
                    navigate('/manager-dashboard', { replace: true })
                    break
                case 'CASHIER':
                case 'CLIENT':
                    console.log('Navigating to /bank-user-dashboard')
                    navigate('/bank-user-dashboard', { replace: true })
                    break
                default:
                    console.log('Role not matched, staying on login. Role was:', role)
                    navigate('/login', { replace: true })
            }
        } catch (err) {
            toast.error(err.response?.data?.message || 'Invalid OTP. Please try again.')
            setOtp(['', '', '', '', '', ''])
            document.getElementById('otp-0')?.focus()
        } finally {
            setLoading(false)
        }
    }

    const handleResendOtp = async () => {
        setOtp(['', '', '', '', '', ''])
        await handleRequestOtp({ preventDefault: () => { } })
    }

    return (
        <div className="min-h-screen w-full flex flex-col items-center justify-center bg-[#d7e7e5] dark:bg-gray-900 px-4 relative">
            {/* Dark Mode Toggle */}
            <button
                onClick={toggleDarkMode}
                className="absolute top-4 right-4 p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition z-10"
            >
                {darkMode ? (
                    <svg className="w-5 h-5 text-yellow-500" fill="currentColor" viewBox="0 0 20 20">
                        <path fillRule="evenodd" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z" clipRule="evenodd" />
                    </svg>
                ) : (
                    <svg className="w-5 h-5 text-gray-700 dark:text-gray-300" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z" />
                    </svg>
                )}
            </button>

            {/* Logo + Bank Title */}
            <div className="text-center mb-6">
                <div className="w-14 h-14 rounded-full bg-[#004d4c] mx-auto mb-3"></div>
                <h1 className="text-3xl sm:text-4xl font-bold text-[#004d4c] tracking-wide">BANKA</h1>
                <p className="text-gray-600 -mt-1 text-sm sm:text-base">Bank of Citizens</p>
            </div>

            <div className="w-full max-w-md sm:max-w-lg bg-[#cee3e1] dark:bg-gray-800 p-4 sm:p-6 md:p-8 rounded-2xl shadow-lg">

                {/* Welcome Title */}
                <div className="text-center mb-4 md:mb-6">
                    <h2 className="text-lg sm:text-xl md:text-2xl font-semibold text-gray-800 dark:text-white">Welcome Back</h2>
                    <p className="text-gray-500 dark:text-gray-400 mt-1 text-xs sm:text-sm md:text-base">Sign in to access your banking services</p>
                </div>

                {/* Error/Success Messages */}
                {error && (
                    <div className="mb-4 p-3 bg-red-100 dark:bg-red-900/30 border border-red-400 dark:border-red-700 text-red-700 dark:text-red-200 rounded-lg text-xs sm:text-sm">
                        {error}
                    </div>
                )}
                {success && (
                    <div className="mb-4 p-3 bg-green-100 dark:bg-green-900/30 border border-green-400 dark:border-green-700 text-green-700 dark:text-green-200 rounded-lg text-xs sm:text-sm">
                        {success}
                    </div>
                )}

                {/* Tabs */}
                <div className="relative flex h-10 sm:h-12 mb-4 md:mb-6 border border-gray-300 dark:border-gray-600 rounded-full overflow-hidden bg-white dark:bg-gray-700">
                    <button
                        onClick={() => setIsLoginMode(true)}
                        className={`w-1/2 text-xs sm:text-sm md:text-lg font-medium relative z-10 ${isLoginMode ? "text-[#004d4c] dark:text-white" : "text-gray-500 dark:text-gray-400"
                            }`}
                    >
                        Sign In
                    </button>

                    <button
                        onClick={() => setIsLoginMode(false)}
                        className={`w-1/2 text-xs sm:text-sm md:text-lg font-medium relative z-10 ${!isLoginMode ? "text-[#004d4c] dark:text-white" : "text-gray-500 dark:text-gray-400"
                            }`}
                    >
                        Sign Up
                    </button>

                    {/* Slide Background */}
                    <div
                        className={`absolute top-0 h-full w-1/2 rounded-full bg-white dark:bg-gray-600 shadow 
                        transition-all duration-300 ${isLoginMode ? "left-0" : "left-1/2"}`}
                    ></div>
                </div>

                {/* FORM */}
                {isLoginMode ? (
                    // LOGIN FORM
                    <form onSubmit={otpSent ? handleVerifyOtp : handleRequestOtp} className="space-y-4">
                        {/* Email */}
                        <input
                            type="email"
                            placeholder="Email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full p-2 sm:p-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-100 dark:bg-gray-700 dark:text-white outline-none text-sm sm:text-base"
                            required
                            disabled={otpSent}
                        />

                        {/* OTP Input (only shown after OTP is sent) */}
                        {otpSent && (
                            <>
                                <div className="flex justify-between gap-1 sm:gap-2">
                                    {otp.map((value, index) => (
                                        <input
                                            key={index}
                                            id={`otp-${index}`}
                                            type="text"
                                            maxLength="1"
                                            value={value}
                                            onChange={(e) => handleOtpChange(e.target.value, index)}
                                            className="flex-1 min-w-[2rem] sm:min-w-[2.5rem] max-w-[3rem] sm:max-w-[3.5rem] h-10 sm:h-12 text-center text-base sm:text-lg rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-100 dark:bg-gray-700 dark:text-white outline-none focus:ring-2 focus:ring-[#004d4c]"
                                            required
                                        />
                                    ))}
                                </div>
                                <div className="text-right">
                                    <button
                                        type="button"
                                        onClick={handleResendOtp}
                                        className="text-[#004d4c] dark:text-[#006b6a] text-xs sm:text-sm cursor-pointer hover:underline disabled:opacity-50"
                                        disabled={loading}
                                    >
                                        Resend OTP
                                    </button>
                                </div>
                            </>
                        )}

                        {/* Button */}
                        <button
                            type="submit"
                            className="w-full p-2 sm:p-3 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg text-sm sm:text-base md:text-lg font-medium hover:opacity-90 transition disabled:opacity-50"
                            disabled={loading}
                        >
                            {loading ? 'Processing...' : (otpSent ? 'Verify & Sign In' : 'Send OTP')}
                        </button>

                        {/* Forgot Password Link */}
                        <div className="text-right">
                            <Link
                                to="/forgot-password"
                                className="text-[#004d4c] dark:text-[#006b6a] text-xs sm:text-sm cursor-pointer hover:underline"
                            >
                                Forgot Password?
                            </Link>
                        </div>

                        {/* Switch */}
                        <p className="text-center text-gray-600 dark:text-gray-400 text-xs sm:text-sm md:text-base">
                            Don't have an account?
                            <span
                                onClick={() => {
                                    setIsLoginMode(false)
                                    setOtpSent(false)
                                    setOtp(['', '', '', '', '', ''])
                                    setEmail('')
                                    setError('')
                                    setSuccess('')
                                }}
                                className="text-[#004d4c] dark:text-[#006b6a] cursor-pointer hover:underline ml-1"
                            >
                                Sign Up
                            </span>
                        </p>
                    </form>
                ) : (
                    // SIGNUP FORM (Disabled - Only managers can create users)
                    <div className="space-y-4">
                        <div className="bg-yellow-50 dark:bg-yellow-900/30 border border-yellow-300 dark:border-yellow-700 rounded-lg p-3 sm:p-4 text-center">
                            <p className="text-yellow-800 dark:text-yellow-200 font-medium mb-2 text-sm sm:text-base">Account Creation Restricted</p>
                            <p className="text-yellow-700 dark:text-yellow-300 text-xs sm:text-sm">
                                New accounts can only be created by authorized managers.
                                Please contact your bank manager to create an account.
                            </p>
                        </div>

                        <p className="text-center text-gray-600 dark:text-gray-400 text-xs sm:text-sm md:text-base">
                            Already have an account?
                            <span
                                onClick={() => {
                                    setIsLoginMode(true)
                                    setError('')
                                    setSuccess('')
                                }}
                                className="text-[#004d4c] dark:text-[#006b6a] cursor-pointer hover:underline ml-1"
                            >
                                Sign In
                            </span>
                        </p>
                    </div>
                )}
            </div>

            {/* Footer */}
            <p className="text-center text-gray-500 dark:text-gray-400 text-xs sm:text-sm mt-4 md:mt-6">
                Secure banking powered by BANKA
            </p>
        </div>
    )
}

export default Login