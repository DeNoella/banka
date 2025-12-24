import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom'
import ProtectedRoute from './auth/ProtectedRoute.jsx'
import DashboardLayout from './Components/layout/DashboardLayout.jsx'

// Public Pages
import Home from './Components/Home.jsx'
import Login from './Components/Login.jsx'
import ForgotPassword from './Components/ForgotPassword.jsx'
import ResetPassword from './Components/ResetPassword.jsx'

// Dashboards
import AdminDashboard from './Components/AdminDashboard.jsx'
import ManagerDashboard from './Components/ManagerDashboard.jsx'
import BankUserDashboard from './Components/BankUserDashboard.jsx'
import CashierDashboard from './Components/CashierDashboard.jsx'

// Admin Pages
import UsersManagement from './Components/admin/UsersManagement.jsx'
import AccountsManagement from './Components/admin/AccountsManagement.jsx'
import LoansManagement from './Components/admin/LoansManagement.jsx'
import GlobalSearch from './Components/admin/GlobalSearch.jsx'

// Manager Pages
import CreateClient from './Components/manager/CreateClient.jsx'
import CreateCashier from './Components/manager/CreateCashier.jsx'

// Cashier Pages
import ProcessTransaction from './Components/cashier/ProcessTransaction.jsx'

// Client Pages
import MyAccounts from './Components/client/MyAccounts.jsx'
import MyTransactions from './Components/client/MyTransactions.jsx'
import MyLoans from './Components/client/MyLoans.jsx'

const App = () => {
  // Menu Configurations
  const adminMenu = [
    { label: 'Dashboard', path: '/admin-dashboard', icon: '📊' },
    { label: 'Users', path: '/admin/users', icon: '👥' },
    { label: 'Accounts', path: '/admin/accounts', icon: '💳' },
    { label: 'Loans', path: '/admin/loans', icon: '💰' },
    { label: 'Global Search', path: '/admin/search', icon: '🔍' },
  ]

  const managerMenu = [
    { label: 'Dashboard', path: '/manager-dashboard', icon: '📊' },
    { label: 'Create Client', path: '/manager/create-client', icon: '➕' },
    { label: 'Create Cashier', path: '/manager/create-cashier', icon: '➕' },
  ]

  const clientMenu = [
    { label: 'Dashboard', path: '/client-dashboard', icon: '📊' },
    { label: 'My Accounts', path: '/client/accounts', icon: '💳' },
    { label: 'Transactions', path: '/client/transactions', icon: '💸' },
    { label: 'Loans', path: '/client/loans', icon: '💰' },
  ]

  const cashierMenu = [
    { label: 'Dashboard', path: '/cashier-dashboard', icon: '📊' },
  ]

  return (
    <div>
      <BrowserRouter>
        <Routes>
          {/* Public Routes */}
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/forgot-password" element={<ForgotPassword />} />
          <Route path="/reset-password" element={<ResetPassword />} />

          {/* Admin Routes */}
          <Route
            path="/admin-dashboard"
            element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <DashboardLayout menuItems={adminMenu}>
                  <AdminDashboard />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/users"
            element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <DashboardLayout menuItems={adminMenu}>
                  <UsersManagement />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/accounts"
            element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <DashboardLayout menuItems={adminMenu}>
                  <AccountsManagement />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/loans"
            element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <DashboardLayout menuItems={adminMenu}>
                  <LoansManagement />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin/search"
            element={
              <ProtectedRoute allowedRoles={['ADMIN']}>
                <DashboardLayout menuItems={adminMenu}>
                  <GlobalSearch />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />

          {/* Manager Routes */}
          <Route
            path="/manager-dashboard"
            element={
              <ProtectedRoute allowedRoles={['MANAGER']}>
                <DashboardLayout menuItems={managerMenu}>
                  <ManagerDashboard />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/manager/create-client"
            element={
              <ProtectedRoute allowedRoles={['MANAGER']}>
                <DashboardLayout menuItems={managerMenu}>
                  <CreateClient />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/manager/create-cashier"
            element={
              <ProtectedRoute allowedRoles={['MANAGER']}>
                <DashboardLayout menuItems={managerMenu}>
                  <CreateCashier />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />

          {/* Client Routes */}
          <Route
            path="/client-dashboard"
            element={
              <ProtectedRoute allowedRoles={['CLIENT']}>
                <DashboardLayout menuItems={clientMenu}>
                  <BankUserDashboard />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/client/accounts"
            element={
              <ProtectedRoute allowedRoles={['CLIENT']}>
                <DashboardLayout menuItems={clientMenu}>
                  <MyAccounts />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/client/transactions"
            element={
              <ProtectedRoute allowedRoles={['CLIENT']}>
                <DashboardLayout menuItems={clientMenu}>
                  <MyTransactions />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/client/loans"
            element={
              <ProtectedRoute allowedRoles={['CLIENT']}>
                <DashboardLayout menuItems={clientMenu}>
                  <MyLoans />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />

          {/* Cashier Routes */}
          <Route
            path="/cashier-dashboard"
            element={
              <ProtectedRoute allowedRoles={['CASHIER']}>
                <DashboardLayout menuItems={cashierMenu}>
                  <CashierDashboard />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />
          <Route
            path="/cashier/transaction"
            element={
              <ProtectedRoute allowedRoles={['CASHIER']}>
                <DashboardLayout menuItems={cashierMenu}>
                  <ProcessTransaction />
                </DashboardLayout>
              </ProtectedRoute>
            }
          />

          {/* Legacy Route */}
          <Route
            path="/bank-user-dashboard"
            element={
              <ProtectedRoute allowedRoles={['CASHIER', 'CLIENT']}>
                <Navigate to="/client-dashboard" replace />
              </ProtectedRoute>
            }
          />

          {/* Catch all */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </div>
  )
}

export default App