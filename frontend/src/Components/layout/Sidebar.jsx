import { useState } from 'react'
import { Link, useLocation } from 'react-router-dom'

const Sidebar = ({ menuItems, onClose }) => {
  const [isCollapsed, setIsCollapsed] = useState(false)
  const location = useLocation()

  const isActive = (path) => location.pathname === path

  const handleLinkClick = () => {
    // Close mobile sidebar when a link is clicked
    if (onClose && window.innerWidth < 768) {
      onClose()
    }
  }

  return (
    <div
      className={`${
        isCollapsed ? 'w-16 sm:w-20' : 'w-56 sm:w-64'
      } h-full bg-white dark:bg-gray-800 border-r border-gray-200 dark:border-gray-700 transition-all duration-300 flex flex-col`}
    >
      {/* Header with Close Button (Mobile) and Toggle (Desktop) */}
      <div className="flex items-center justify-between p-2 md:p-4 border-b border-gray-200 dark:border-gray-700">
        {/* Close Button (Mobile Only) */}
        {onClose && (
          <button
            onClick={onClose}
            className="md:hidden p-2 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition text-gray-600 dark:text-gray-300"
            aria-label="Close menu"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        )}
        {/* Toggle Button (Desktop Only) */}
        <button
          onClick={() => setIsCollapsed(!isCollapsed)}
          className="hidden md:block p-2 hover:bg-gray-100 dark:hover:bg-gray-700 rounded-lg transition text-gray-600 dark:text-gray-300"
          aria-label="Toggle sidebar"
        >
          <svg
            className={`w-5 h-5 transition-transform ${isCollapsed ? 'rotate-180' : ''}`}
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 19l-7-7 7-7m8 14l-7-7 7-7" />
          </svg>
        </button>
      </div>

      {/* Menu Items */}
      <nav className="flex-1 px-2 py-2 md:py-4 space-y-1 md:space-y-2 overflow-y-auto">
        {menuItems.map((item, index) => (
          <Link
            key={index}
            to={item.path}
            onClick={handleLinkClick}
            className={`flex items-center gap-2 sm:gap-3 px-3 sm:px-4 py-2 sm:py-3 rounded-lg transition text-sm sm:text-base ${
              isActive(item.path)
                ? 'bg-[#004d4c] dark:bg-[#006b6a] text-white'
                : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-gray-700'
            }`}
            title={item.label}
          >
            {/* Icon */}
            {item.icon && <span className="text-lg sm:text-xl">{item.icon}</span>}

            {/* Label */}
            {!isCollapsed && <span className="font-medium">{item.label}</span>}
          </Link>
        ))}
      </nav>
    </div>
  )
}

export default Sidebar
