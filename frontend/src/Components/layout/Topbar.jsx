import { useNavigate } from 'react-router-dom'
import { useTheme } from '../../context/ThemeContext'

const Topbar = ({ onMenuClick }) => {
  const navigate = useNavigate()
  const { darkMode, toggleDarkMode } = useTheme()

  const handleLogout = () => {
    localStorage.clear()
    navigate('/login')
  }

  const firstName = localStorage.getItem('firstName') || 'User'
  const lastName = localStorage.getItem('lastName') || ''
  const role = localStorage.getItem('role') || 'GUEST'

  return (
    <div className="h-14 sm:h-16 bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 flex items-center justify-between px-3 sm:px-4 md:px-6 shadow-sm">
      {/* Left: Menu Button (Mobile) + Logo */}
      <div className="flex items-center gap-2 sm:gap-3">
        {/* Mobile Menu Button */}
        <button
          onClick={onMenuClick}
          className="md:hidden p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition"
          aria-label="Toggle menu"
        >
          <svg className="w-6 h-6 text-gray-600 dark:text-gray-300" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
          </svg>
        </button>
        <div className="w-8 h-8 sm:w-10 sm:h-10 rounded-full bg-[#004d4c] dark:bg-[#006b6a] flex items-center justify-center">
          <span className="text-white font-bold text-base sm:text-lg">B</span>
        </div>
        <h1 className="text-lg sm:text-xl font-bold text-[#004d4c] dark:text-white">BANKA</h1>
      </div>

      {/* Right: User info, Dark mode toggle, Logout */}
      <div className="flex items-center gap-2 sm:gap-4">
        {/* User Info */}
        <div className="text-right hidden lg:block">
          <p className="text-sm font-semibold text-gray-800 dark:text-gray-200">{firstName} {lastName}</p>
          <p className="text-xs text-gray-500 dark:text-gray-400">{role}</p>
        </div>

        {/* Dark Mode Toggle */}
        <button
          onClick={toggleDarkMode}
          className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition"
          title={darkMode ? 'Switch to Light Mode' : 'Switch to Dark Mode'}
        >
          {darkMode ? (
            // Sun icon
            <svg className="w-5 h-5 text-yellow-500" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M10 2a1 1 0 011 1v1a1 1 0 11-2 0V3a1 1 0 011-1zm4 8a4 4 0 11-8 0 4 4 0 018 0zm-.464 4.95l.707.707a1 1 0 001.414-1.414l-.707-.707a1 1 0 00-1.414 1.414zm2.12-10.607a1 1 0 010 1.414l-.706.707a1 1 0 11-1.414-1.414l.707-.707a1 1 0 011.414 0zM17 11a1 1 0 100-2h-1a1 1 0 100 2h1zm-7 4a1 1 0 011 1v1a1 1 0 11-2 0v-1a1 1 0 011-1zM5.05 6.464A1 1 0 106.465 5.05l-.708-.707a1 1 0 00-1.414 1.414l.707.707zm1.414 8.486l-.707.707a1 1 0 01-1.414-1.414l.707-.707a1 1 0 011.414 1.414zM4 11a1 1 0 100-2H3a1 1 0 000 2h1z" clipRule="evenodd" />
            </svg>
          ) : (
            // Moon icon
            <svg className="w-5 h-5 text-gray-700" fill="currentColor" viewBox="0 0 20 20">
              <path d="M17.293 13.293A8 8 0 016.707 2.707a8.001 8.001 0 1010.586 10.586z" />
            </svg>
          )}
        </button>

        {/* Logout Button */}
        <button
          onClick={handleLogout}
          className="px-2 sm:px-4 py-1.5 sm:py-2 bg-red-600 hover:bg-red-700 text-white rounded-lg text-xs sm:text-sm font-medium transition"
          title="Logout"
        >
          <span className="hidden sm:inline">Logout</span>
          <span className="sm:hidden">🚪</span>
        </button>
      </div>
    </div>
  )
}

export default Topbar
