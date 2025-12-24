import { useState, useEffect } from 'react'
import { toast } from 'react-toastify'
import axios from '../../api/axios'
import DataTable from '../common/DataTable'

const LoansManagement = () => {
  const [loans, setLoans] = useState([])
  const [currentPage, setCurrentPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(false)
  const [searchTerm, setSearchTerm] = useState('')

  useEffect(() => {
    fetchLoans()
  }, [currentPage, searchTerm])

  const fetchLoans = async () => {
    setLoading(true)
    try {
      const params = {
        page: currentPage,
        size: 5
      }
      if (searchTerm) {
        params.search = searchTerm
      }

      const response = await axios.get('/admin/loans', { params })
      setLoans(response.data.content || [])
      setTotalPages(response.data.totalPages || 0)
    } catch (error) {
      toast.error('Failed to fetch loans')
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
      key: 'borrower',
      header: 'Borrower',
      render: (row) => row.userFirstName && row.userLastName ?
        `${row.userFirstName} ${row.userLastName}` : 'N/A'
    },
    {
      key: 'amount',
      header: 'Amount',
      render: (row) => `RWF ${row.amount?.toLocaleString() || '0'}`
    },
    {
      key: 'interestRate',
      header: 'Interest Rate',
      render: (row) => `${row.interestRate || 0}%`
    },
    {
      key: 'startDate',
      header: 'Start Date',
      render: (row) => row.startDate ? new Date(row.startDate).toLocaleDateString() : 'N/A'
    },
    {
      key: 'endDate',
      header: 'End Date',
      render: (row) => row.endDate ? new Date(row.endDate).toLocaleDateString() : 'N/A'
    },
    {
      key: 'loanStatus',
      header: 'Status',
      render: (row) => (
        <span className={`px-2 py-1 rounded text-xs font-semibold ${row.loanStatus === 'APPROVED' ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
          row.loanStatus === 'PENDING' ? 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200' :
            'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200'
          }`}>
          {row.loanStatus}
        </span>
      )
    },
    {
      key: 'isRepaid',
      header: 'Repayment',
      render: (row) => (
        <span className={`px-2 py-1 rounded text-xs font-semibold ${row.isRepaid ? 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200' :
          'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200'
          }`}>
          {row.isRepaid ? 'Repaid' : 'Outstanding'}
        </span>
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
    <div className="space-y-4 md:space-y-6">
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-3 sm:gap-0">
        <h1 className="text-2xl md:text-3xl font-bold text-gray-800 dark:text-white">Loans Management</h1>
      </div>

      <DataTable
        columns={columns}
        data={loans}
        currentPage={currentPage}
        totalPages={totalPages}
        onPageChange={setCurrentPage}
        onSearch={handleSearch}
        searchPlaceholder="Search loans by borrower, amount, or status..."
        loading={loading}
      />
    </div>
  )
}

export default LoansManagement
