import { useState, useEffect } from 'react'
import { toast } from 'react-toastify'
import axios from '../../api/axios'
import DataTable from '../common/DataTable'

const AccountsManagement = () => {
  const [accounts, setAccounts] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')

  useEffect(() => {
    fetchAccounts()
  }, [currentPage, searchTerm])

  const fetchAccounts = async () => {
    setLoading(true)
    try {
      const params = {
        page: currentPage,
        size: 5
      }
      if (searchTerm) {
        params.search = searchTerm
      }

      const response = await axios.get('/admin/accounts', { params })
      setAccounts(response.data.content || [])
      setTotalPages(response.data.totalPages || 0)
    } catch (error) {
      toast.error('Failed to fetch accounts')
      console.error(error)
    } finally {
      setLoading(false)
    }
  }

  const handleSearch = (term) => {
    setSearchTerm(term)
    setCurrentPage(0)
  }

  const columns = [
    {
      key: 'accountNumber',
      header: 'Account Number'
    },
    {
      key: 'accountType',
      header: 'Type',
      render: (row) => (
        <span className="px-2 py-1 bg-blue-100 dark:bg-blue-900 text-blue-800 dark:text-blue-200 rounded text-xs font-semibold">
          {row.accountType}
        </span>
      )
    },
    {
      key: 'balance',
      header: 'Balance',
      render: (row) => `RWF ${row.balance?.toLocaleString() || '0'}`
    },
    {
      key: 'user',
      header: 'Owner',
      render: (row) => row.user ? `${row.user.firstName} ${row.user.lastName}` : 'N/A'
    },
    {
      key: 'user.email',
      header: 'Email',
      render: (row) => row.user?.email || 'N/A'
    },
    {
      key: 'verified',
      header: 'Status',
      render: (row) => (
        <div className="flex gap-2">
          <span className={`px-2 py-1 rounded text-xs font-semibold ${row.verified ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
            'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200'
            }`}>
            {row.verified ? 'Verified' : 'Pending'}
          </span>
          <span className={`px-2 py-1 rounded text-xs font-semibold ${row.active ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
            'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
            }`}>
            {row.active ? 'Active' : 'Inactive'}
          </span>
        </div>
      )
    }
  ]

  const menuItems = [
    { label: 'Dashboard', path: '/admin-dashboard', icon: '📊' },
    { label: 'Users', path: '/admin/users', icon: '👥' },
    { label: 'Accounts', path: '/admin/accounts', icon: '💳' },
    { label: 'Loans', path: '/admin/loans', icon: '💰' },
    { label: 'Search', path: '/admin/search', icon: '🔍' }
  ]

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold text-gray-800 dark:text-white">Accounts Management</h1>
      </div>

      <DataTable
        columns={columns}
        data={accounts}
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
        onSearch={handleSearch}
        searchPlaceholder="Search accounts by number, type, or owner..."
        loading={loading}
      />
    </div>
  )
}

export default AccountsManagement
