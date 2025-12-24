import { useState } from 'react'
import { Link } from 'react-router-dom'
import { toast } from 'react-toastify'
import axios from '../api/axios'
import { useTheme } from '../context/ThemeContext'

const ForgotPassword = () => {
  const [email, setEmail] = useState('')
  const [loading, setLoading] = useState(false)
  const [sent, setSent] = useState(false)
  const { darkMode, toggleDarkMode } = useTheme()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)

    try {
      const response = await axios.post('/auth/forgot-password', { email })
      toast.success(response.data.message || 'Password reset link sent to your email!')
      setSent(true)
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to send reset link')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen w-full flex flex-col items-center justify-center bg-[#d7e7e5] dark:bg-gray-900 px-4 relative">
      {/* Dark Mode Toggle */}
      <button
        onClick={toggleDarkMode}
        className="absolute top-4 right-4 p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition z-10"
        aria-label="Toggle dark mode"
      >
        {darkMode ? (
          <svg className="w-5 h-5 text-yellow-500" fill="currentColor" viewBox="0 0 20 20">
            <path fillRule="evenodd" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z" clipRule="evenodd" />
          </svg>
        ) : (
          <svg className="w-5 h-5 text-gray-700" fill="currentColor" viewBox="0 0 20 20">
            <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z" />
          </svg>
        )}
      </button>

      {/* Logo */}
      <div className="text-center mb-6">
        <div className="w-14 h-14 rounded-full bg-[#004d4c] dark:bg-[#006b6a] mx-auto mb-3"></div>
        <h1 className="text-3xl sm:text-4xl font-bold text-[#004d4c] dark:text-white tracking-wide">BANKA</h1>
        <p className="text-gray-600 dark:text-gray-400 -mt-1 text-sm sm:text-base">Bank of Citizens</p>
      </div>

      <div className="w-full max-w-md bg-[#cee3e1] dark:bg-gray-800 p-4 sm:p-6 md:p-8 rounded-2xl shadow-lg">
        <div className="text-center mb-4 md:mb-6">
          <h2 className="text-lg sm:text-xl md:text-2xl font-semibold text-gray-800 dark:text-white">Forgot Password</h2>
          <p className="text-gray-600 dark:text-gray-400 mt-1 text-xs sm:text-sm md:text-base">
            Enter your email to receive a password reset link
          </p>
        </div>

        {!sent ? (
          <form onSubmit={handleSubmit} className="space-y-4">
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full p-2 sm:p-3 rounded-lg border border-gray-300 dark:border-gray-600 bg-gray-100 dark:bg-gray-700 dark:text-white outline-none text-sm sm:text-base"
              required
            />

            <button
              type="submit"
              disabled={loading}
              className="w-full p-2 sm:p-3 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg text-sm sm:text-base md:text-lg font-medium hover:opacity-90 transition disabled:opacity-50"
            >
              {loading ? 'Sending...' : 'Send Reset Link'}
            </button>

            <p className="text-center text-gray-600 dark:text-gray-400 text-xs sm:text-sm md:text-base">
              Remember your password?{' '}
              <Link to="/login" className="text-[#004d4c] dark:text-[#006b6a] hover:underline">
                Sign In
              </Link>
            </p>
          </form>
        ) : (
          <div className="space-y-4">
            <div className="bg-green-100 dark:bg-green-900/30 border border-green-400 dark:border-green-700 rounded-lg p-4 text-center">
              <p className="text-green-800 dark:text-green-200 font-medium mb-2">Email Sent!</p>
              <p className="text-green-700 dark:text-green-300 text-sm">
                Check your email for the password reset link. The link will expire in 1 hour.
              </p>
            </div>

            <Link
              to="/login"
              className="block w-full p-2 sm:p-3 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg text-sm sm:text-base md:text-lg font-medium hover:opacity-90 transition text-center"
            >
              Back to Login
            </Link>
          </div>
        )}
      </div>
    </div>
  )
}

export default ForgotPassword
