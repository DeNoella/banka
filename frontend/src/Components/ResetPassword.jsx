import { useState, useEffect } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { toast } from "react-toastify";
import axios from "../api/axios";
import { useTheme } from "../context/ThemeContext";

const ResetPassword = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const { darkMode, toggleDarkMode } = useTheme();
  const [token, setToken] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [submitted, setSubmitted] = useState(false);
  const [passwordError, setPasswordError] = useState('');
  const [confirmPasswordError, setConfirmPasswordError] = useState('');

  useEffect(() => {
    const tokenParam = searchParams.get('token');
    if (tokenParam) {
      setToken(tokenParam);
    } else {
      toast.error('Invalid reset link');
      navigate('/forgot-password');
    }
  }, [searchParams, navigate]);

  const validatePassword = (password) => {
    if (password.length < 6) {
      return 'Password must be at least 6 characters long';
    }
    return '';
  };

  const validateConfirmPassword = (password, confirm) => {
    if (confirm && password !== confirm) {
      return 'Passwords do not match';
    }
    return '';
  };

  const handlePasswordChange = (value) => {
    setNewPassword(value);
    setPasswordError(validatePassword(value));
    if (confirmPassword) {
      setConfirmPasswordError(validateConfirmPassword(value, confirmPassword));
    }
  };

  const handleConfirmPasswordChange = (value) => {
    setConfirmPassword(value);
    setConfirmPasswordError(validateConfirmPassword(newPassword, value));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validate passwords
    const pwdError = validatePassword(newPassword);
    const confirmError = validateConfirmPassword(newPassword, confirmPassword);

    setPasswordError(pwdError);
    setConfirmPasswordError(confirmError);

    if (pwdError || confirmError) {
      if (pwdError) toast.error(pwdError);
      if (confirmError) toast.error(confirmError);
      return;
    }

    setLoading(true);
    try {
      const response = await axios.post('/auth/reset-password', {
        token,
        newPassword
      });
      toast.success(response.data.message || 'Password reset successfully!');
      setSubmitted(true);
    } catch (error) {
      toast.error(error.response?.data?.message || 'Failed to reset password');
    } finally {
      setLoading(false);
    }
  };

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

      {/* Logo + Title */}
      <div className="text-center mb-6">
        <div className="w-14 h-14 rounded-full bg-[#004d4c] dark:bg-[#006b6a] mx-auto mb-3"></div>
        <h1 className="text-3xl sm:text-4xl font-bold text-[#004d4c] dark:text-white tracking-wide">BANKA</h1>
        <p className="text-gray-600 dark:text-gray-400 -mt-1 text-sm sm:text-base">Bank of Citizens</p>
      </div>

      <div className="w-full max-w-md sm:max-w-lg bg-[#cee3e1] dark:bg-gray-800 p-4 sm:p-6 md:p-8 rounded-2xl shadow-lg">

        {/* Title */}
        <div className="text-center mb-4 md:mb-6">
          <h2 className="text-lg sm:text-xl md:text-2xl font-semibold text-gray-800 dark:text-white">Reset Password</h2>
          <p className="text-gray-600 dark:text-gray-400 mt-1 text-xs sm:text-sm md:text-base">
            Enter your new password and confirm it to reset your account password
          </p>
        </div>

        {!submitted ? (
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                New Password <span className="text-red-500">*</span>
              </label>
              <input
                type="password"
                placeholder="Enter new password (min. 6 characters)"
                value={newPassword}
                onChange={(e) => handlePasswordChange(e.target.value)}
                className={`w-full p-2 sm:p-3 rounded-lg border ${
                  passwordError 
                    ? 'border-red-500 dark:border-red-500' 
                    : 'border-gray-300 dark:border-gray-600'
                } bg-gray-100 dark:bg-gray-700 dark:text-white outline-none focus:ring-2 focus:ring-[#004d4c] text-sm sm:text-base`}
                required
                minLength={6}
              />
              {passwordError && (
                <p className="mt-1 text-xs text-red-600 dark:text-red-400">{passwordError}</p>
              )}
              {newPassword && !passwordError && (
                <p className="mt-1 text-xs text-green-600 dark:text-green-400">✓ Password is valid</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">
                Confirm New Password <span className="text-red-500">*</span>
              </label>
              <input
                type="password"
                placeholder="Confirm your new password"
                value={confirmPassword}
                onChange={(e) => handleConfirmPasswordChange(e.target.value)}
                className={`w-full p-2 sm:p-3 rounded-lg border ${
                  confirmPasswordError 
                    ? 'border-red-500 dark:border-red-500' 
                    : confirmPassword && newPassword === confirmPassword
                    ? 'border-green-500 dark:border-green-500'
                    : 'border-gray-300 dark:border-gray-600'
                } bg-gray-100 dark:bg-gray-700 dark:text-white outline-none focus:ring-2 focus:ring-[#004d4c] text-sm sm:text-base`}
                required
                minLength={6}
              />
              {confirmPasswordError && (
                <p className="mt-1 text-xs text-red-600 dark:text-red-400">{confirmPasswordError}</p>
              )}
              {confirmPassword && !confirmPasswordError && newPassword === confirmPassword && (
                <p className="mt-1 text-xs text-green-600 dark:text-green-400">✓ Passwords match</p>
              )}
            </div>

            <button
              type="submit"
              disabled={loading || !!passwordError || !!confirmPasswordError || !newPassword || !confirmPassword}
              className="w-full p-2 sm:p-3 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg text-sm sm:text-base md:text-lg font-medium hover:opacity-90 transition disabled:opacity-50 disabled:cursor-not-allowed"
            >
              {loading ? 'Resetting Password...' : 'Reset Password'}
            </button>

            <p className="text-center text-gray-600 dark:text-gray-400 text-sm sm:text-base">
              Go back to{" "}
              <Link to="/login" className="text-[#004d4c] dark:text-[#006b6a] hover:underline">
                Sign In
              </Link>
            </p>
          </form>
        ) : (
          <div className="text-center space-y-4">
            <p className="text-green-600 dark:text-green-400 font-medium text-sm sm:text-base">
              ✓ Your password has been reset successfully.
            </p>

            <Link
              to="/login"
              className="block w-full p-2 sm:p-3 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg text-sm sm:text-base md:text-lg font-medium hover:opacity-90 transition"
            >
              Back to Login
            </Link>
          </div>
        )}
      </div>

      <p className="text-center text-gray-500 dark:text-gray-400 text-xs sm:text-sm mt-6">
        Secure banking powered by BANKA
      </p>
    </div>
  );
};

export default ResetPassword;
