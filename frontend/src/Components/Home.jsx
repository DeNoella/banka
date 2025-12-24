import { Link } from 'react-router-dom'
import { useTheme } from '../context/ThemeContext'

const Home = () => {
  const { darkMode, toggleDarkMode } = useTheme()

  return (
    <div className="min-h-screen bg-[#d7e7e5] dark:bg-gray-900">
      {/* Header */}
      <header className="bg-white dark:bg-gray-800 shadow-sm">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
          {/* Logo */}
          <div className="flex items-center gap-3">
            <div className="w-10 h-10 rounded-full bg-[#004d4c] dark:bg-[#006b6a]"></div>
            <div>
              <h1 className="text-2xl font-bold text-[#004d4c] dark:text-white">BANKA</h1>
              <p className="text-xs text-gray-600 dark:text-gray-400">Bank of Citizens</p>
            </div>
          </div>

          {/* Navigation */}
          <div className="flex items-center gap-4">
            <button
              onClick={toggleDarkMode}
              className="p-2 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition"
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

            <Link
              to="/login"
              className="px-6 py-2 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg hover:opacity-90 transition font-medium"
            >
              Login
            </Link>
          </div>
        </div>
      </header>

      {/* Hero Section */}
      <section className="relative px-4 sm:px-6 lg:px-8 py-20 md:py-32 overflow-hidden">
        {/* Background Gradient Blob */}
        <div className="absolute top-0 left-0 w-full h-full overflow-hidden -z-10">
          <div className="absolute -top-[50%] -left-[20%] w-[70%] h-[70%] rounded-full bg-gradient-to-r from-[#004d4c]/20 to-[#006b6a]/20 blur-3xl animate-pulse"></div>
          <div className="absolute top-[20%] -right-[20%] w-[60%] h-[60%] rounded-full bg-gradient-to-l from-[#004d4c]/10 to-teal-400/10 blur-3xl animate-pulse delay-1000"></div>
        </div>

        <div className="max-w-7xl mx-auto text-center relative z-10">
          <h2 className="text-4xl sm:text-5xl md:text-7xl font-extrabold text-[#004d4c] dark:text-white mb-6 md:mb-8 tracking-tight leading-tight">
            Banking Reimagined <br />
            <span className="text-transparent bg-clip-text bg-gradient-to-r from-[#004d4c] to-teal-500 dark:from-teal-300 dark:to-white">
              For The Future
            </span>
          </h2>
          <p className="text-lg sm:text-xl md:text-2xl text-gray-700 dark:text-gray-300 mb-8 md:mb-10 max-w-3xl mx-auto font-light px-4">
            Experience the next generation of financial management. Secure, seamless, and smart banking solutions designed for your lifestyle.
          </p>
          <div className="flex flex-col sm:flex-row justify-center gap-4 md:gap-6 px-4">
            <Link
              to="/login"
              className="px-8 md:px-10 py-3 md:py-4 bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-full text-base md:text-lg font-bold hover:shadow-xl hover:scale-105 transition-all duration-300 shadow-lg"
            >
              Get Started
            </Link>
          </div>
        </div>
      </section>

      {/* About Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 md:py-24">
        <div className="text-center mb-12">
          <h3 className="text-3xl md:text-4xl font-bold text-[#004d4c] dark:text-white mb-4">
            About Bank of Citizens
          </h3>
          <p className="text-lg md:text-xl text-gray-700 dark:text-gray-300 max-w-3xl mx-auto">
            Bank of Citizens (BANKA) is Rwanda's premier financial institution, committed to providing secure, 
            innovative, and accessible banking services to all citizens. We empower individuals and businesses 
            with cutting-edge financial solutions.
          </p>
        </div>
      </section>

      {/* Services Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 md:py-24">
        <h3 className="text-3xl md:text-4xl font-bold text-center text-[#004d4c] dark:text-white mb-12 md:mb-16">
          Why Choose BANKA?
        </h3>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 md:gap-10">
          {/* Service 1 */}
          <div className="group bg-white dark:bg-gray-800 p-6 md:p-8 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 border border-transparent hover:border-teal-100 dark:hover:border-teal-900">
            <div className="w-14 h-14 md:w-16 md:h-16 bg-teal-50 dark:bg-gray-700 rounded-2xl flex items-center justify-center mb-4 md:mb-6 group-hover:scale-110 transition-transform duration-300">
              <span className="text-3xl md:text-4xl">💳</span>
            </div>
            <h4 className="text-xl md:text-2xl font-bold text-gray-900 dark:text-white mb-3 md:mb-4">
              Smart Accounts
            </h4>
            <p className="text-sm md:text-base text-gray-600 dark:text-gray-400 leading-relaxed">
              Open savings and checking accounts instantly. Manage your finances with our intuitive dashboard and real-time analytics.
            </p>
          </div>

          {/* Service 2 */}
          <div className="group bg-white dark:bg-gray-800 p-6 md:p-8 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 border border-transparent hover:border-teal-100 dark:hover:border-teal-900">
            <div className="w-14 h-14 md:w-16 md:h-16 bg-teal-50 dark:bg-gray-700 rounded-2xl flex items-center justify-center mb-4 md:mb-6 group-hover:scale-110 transition-transform duration-300">
              <span className="text-3xl md:text-4xl">🚀</span>
            </div>
            <h4 className="text-xl md:text-2xl font-bold text-gray-900 dark:text-white mb-3 md:mb-4">
              Instant Loans
            </h4>
            <p className="text-sm md:text-base text-gray-600 dark:text-gray-400 leading-relaxed">
              Need funds? Apply for personal or business loans with competitive rates and get approved in minutes, not days.
            </p>
          </div>

          {/* Service 3 */}
          <div className="group bg-white dark:bg-gray-800 p-6 md:p-8 rounded-2xl shadow-lg hover:shadow-2xl transition-all duration-300 transform hover:-translate-y-2 border border-transparent hover:border-teal-100 dark:hover:border-teal-900">
            <div className="w-14 h-14 md:w-16 md:h-16 bg-teal-50 dark:bg-gray-700 rounded-2xl flex items-center justify-center mb-4 md:mb-6 group-hover:scale-110 transition-transform duration-300">
              <span className="text-3xl md:text-4xl">🛡️</span>
            </div>
            <h4 className="text-xl md:text-2xl font-bold text-gray-900 dark:text-white mb-3 md:mb-4">
              Bank-Grade Security
            </h4>
            <p className="text-sm md:text-base text-gray-600 dark:text-gray-400 leading-relaxed">
              Your security is our priority. We use advanced encryption and 2-factor authentication (OTP) for every transaction.
            </p>
          </div>
        </div>
      </section>

      {/* Business Info Section */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-16 md:py-24 bg-white dark:bg-gray-800">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-8 md:gap-12">
          <div>
            <h3 className="text-2xl md:text-3xl font-bold text-[#004d4c] dark:text-white mb-4">
              Our Mission
            </h3>
            <p className="text-gray-700 dark:text-gray-300 leading-relaxed mb-6">
              To provide accessible, secure, and innovative banking services that empower every citizen 
              to achieve their financial goals. We are committed to financial inclusion and excellence 
              in customer service.
            </p>
          </div>
          <div>
            <h3 className="text-2xl md:text-3xl font-bold text-[#004d4c] dark:text-white mb-4">
              Contact Information
            </h3>
            <div className="space-y-3 text-gray-700 dark:text-gray-300">
              <p><strong>Address:</strong> Kigali, Rwanda</p>
              <p><strong>Email:</strong> info@banka.rw</p>
              <p><strong>Phone:</strong> +250 788 123 456</p>
              <p><strong>Hours:</strong> Monday - Friday, 8:00 AM - 5:00 PM</p>
            </div>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-white dark:bg-gray-800 mt-12 md:mt-20">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6 md:py-8">
          <div className="text-center text-sm md:text-base text-gray-600 dark:text-gray-400">
            <p>&copy; {new Date().getFullYear()} Bank of Citizens (BANKA). All rights reserved.</p>
            <p className="mt-2">Secure banking powered by innovation.</p>
          </div>
        </div>
      </footer>
    </div>
  )
}

export default Home
