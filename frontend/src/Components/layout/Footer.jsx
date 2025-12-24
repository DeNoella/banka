const Footer = () => {
  return (
    <footer className="bg-white dark:bg-gray-800 border-t border-gray-200 dark:border-gray-700 py-4 px-6">
      <div className="text-center text-sm text-gray-600 dark:text-gray-400">
        <p>&copy; {new Date().getFullYear()} Bank of Citizens (BANKA). All rights reserved.</p>
        <p className="mt-1">Secure banking powered by innovation.</p>
      </div>
    </footer>
  )
}

export default Footer
