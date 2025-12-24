import { useState } from 'react'
import { toast } from 'react-toastify'
import axios from '../../api/axios'

const GlobalSearch = () => {
  const [query, setQuery] = useState('')
  const [results, setResults] = useState(null)
  const [loading, setLoading] = useState(false)

  const handleSearch = async (e) => {
    e.preventDefault()
    if (!query.trim()) {
      toast.error('Please enter a search term')
      return
    }

    setLoading(true)
    try {
      const response = await axios.get('/admin/search', {
        params: { q: query, page: 0, size: 20 }
      })
      setResults(response.data)
    } catch (error) {
      toast.error('Search failed')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }



  return (
    <div className="space-y-4 md:space-y-6">
      <h1 className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white">Global Search</h1>

      {/* Search Form */}
      <form onSubmit={handleSearch} className="flex flex-col sm:flex-row gap-2 sm:gap-3">
        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          placeholder="Search users, accounts, loans, transactions..."
          className="flex-1 px-3 sm:px-4 py-2 sm:py-3 text-sm sm:text-base border border-gray-300 dark:border-gray-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#004d4c] dark:bg-gray-700 dark:text-white"
        />
        <button
          type="submit"
          disabled={loading}
          className="px-4 sm:px-6 py-2 sm:py-3 text-sm sm:text-base bg-[#004d4c] dark:bg-[#006b6a] text-white rounded-lg hover:opacity-90 disabled:opacity-50 font-medium"
        >
          {loading ? 'Searching...' : 'Search'}
        </button>
      </form>

      {/* Results */}
      {results && (
        <div className="space-y-4 md:space-y-6">
          <div className="text-sm sm:text-base text-gray-600 dark:text-gray-400">
            Search results for: <span className="font-semibold text-gray-800 dark:text-white">"{results.query}"</span>
          </div>

          {/* Users Results */}
          {results.users && results.users.content && results.users.content.length > 0 && (
            <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
              <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">
                Users ({results.users.totalElements})
              </h2>
              <div className="space-y-3">
                {results.users.content.map((user) => (
                  <div
                    key={user.userId}
                    className="p-3 md:p-4 border border-gray-200 dark:border-gray-700 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700"
                  >
                    <div className="flex flex-col sm:flex-row justify-between items-start gap-2">
                      <div>
                        <h3 className="font-semibold text-gray-800 dark:text-white">
                          {user.firstName} {user.lastName}
                        </h3>
                        <p className="text-xs md:text-sm text-gray-600 dark:text-gray-400">{user.email}</p>
                        <p className="text-xs md:text-sm text-gray-600 dark:text-gray-400">{user.phoneNumber}</p>
                      </div>
                      <span className={`px-2 py-1 rounded text-xs font-semibold ${user.userRole === 'ADMIN' ? 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200' :
                        user.userRole === 'MANAGER' ? 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200' :
                          user.userRole === 'CASHIER' ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
                            'bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200'
                        }`}>
                        {user.userRole}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Accounts Results */}
          {results.accounts && results.accounts.content && results.accounts.content.length > 0 && (
            <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
              <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">
                Accounts ({results.accounts.totalElements})
              </h2>
              <div className="space-y-3">
                {results.accounts.content.map((account) => (
                  <div
                    key={account.accountId}
                    className="p-3 md:p-4 border border-gray-200 dark:border-gray-700 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700"
                  >
                    <div className="flex flex-col sm:flex-row justify-between items-start gap-2">
                      <div>
                        <h3 className="font-semibold text-gray-800 dark:text-white">
                          {account.accountNumber}
                        </h3>
                        <p className="text-xs md:text-sm text-gray-600 dark:text-gray-400">
                          Type: {account.accountType} | Balance: RWF {account.balance?.toLocaleString() || '0'}
                        </p>
                        {account.user && (
                          <p className="text-xs md:text-sm text-gray-600 dark:text-gray-400">
                            Owner: {account.user.firstName} {account.user.lastName}
                          </p>
                        )}
                      </div>
                      <span className={`px-2 py-1 rounded text-xs font-semibold ${account.active ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
                        'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
                        }`}>
                        {account.active ? 'Active' : 'Inactive'}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* Loans Results */}
          {results.loans && results.loans.content && results.loans.content.length > 0 && (
            <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 md:p-6">
              <h2 className="text-lg md:text-xl font-bold text-gray-800 dark:text-white mb-4">
                Loans ({results.loans.totalElements})
              </h2>
              <div className="space-y-3">
                {results.loans.content.map((loan) => (
                  <div
                    key={loan.loanId}
                    className="p-3 md:p-4 border border-gray-200 dark:border-gray-700 rounded-lg hover:bg-gray-50 dark:hover:bg-gray-700"
                  >
                    <div className="flex flex-col sm:flex-row justify-between items-start gap-2">
                      <div>
                        <h3 className="font-semibold text-gray-800 dark:text-white">
                          Loan Amount: RWF {loan.amount?.toLocaleString() || '0'}
                        </h3>
                        <p className="text-xs md:text-sm text-gray-600 dark:text-gray-400">
                          Interest Rate: {loan.interestRate || 0}% | Status: {loan.loanStatus}
                        </p>
                        {loan.userFirstName && (
                          <p className="text-xs md:text-sm text-gray-600 dark:text-gray-400">
                            Borrower: {loan.userFirstName} {loan.userLastName}
                          </p>
                        )}
                      </div>
                      <span className={`px-2 py-1 rounded text-xs font-semibold ${loan.loanStatus === 'APPROVED' ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
                        loan.loanStatus === 'PENDING' ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200' :
                          'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
                        }`}>
                        {loan.loanStatus}
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          )}

          {/* No Results */}
          {(!results.users || !results.users.content || results.users.content.length === 0) &&
            (!results.accounts || !results.accounts.content || results.accounts.content.length === 0) &&
            (!results.loans || !results.loans.content || results.loans.content.length === 0) && (
              <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-6 text-center text-gray-500 dark:text-gray-400">
                No results found for "{results.query}"
              </div>
            )}
        </div>
      )}
    </div>
  )
}

export default GlobalSearch
