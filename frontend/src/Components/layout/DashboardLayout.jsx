import { useState } from 'react'
import Topbar from './Topbar'
import Sidebar from './Sidebar'
import Footer from './Footer'

const DashboardLayout = ({ children, menuItems }) => {
  const [sidebarOpen, setSidebarOpen] = useState(false)

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex flex-col">
      {/* Topbar */}
      <Topbar onMenuClick={() => setSidebarOpen(!sidebarOpen)} />

      {/* Main Content Area */}
      <div className="flex flex-1 relative">
        {/* Mobile Sidebar Overlay */}
        {sidebarOpen && (
          <div
            className="fixed inset-0 bg-black bg-opacity-50 z-40 md:hidden"
            onClick={() => setSidebarOpen(false)}
          />
        )}

        {/* Sidebar */}
        <div className={`${sidebarOpen ? 'translate-x-0' : '-translate-x-full'} md:translate-x-0 fixed md:static inset-y-0 left-0 z-50 transition-transform duration-300`}>
          <Sidebar menuItems={menuItems} onClose={() => setSidebarOpen(false)} />
        </div>

        {/* Page Content */}
        <main className="flex-1 p-3 sm:p-4 md:p-6 overflow-y-auto w-full md:w-auto">
          {children}
        </main>
      </div>

      {/* Footer */}
      <Footer />
    </div>
  )
}

export default DashboardLayout
