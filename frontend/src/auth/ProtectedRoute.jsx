import React from 'react'
import { Navigate } from 'react-router-dom'
import AuthService from './AuthService'

const ProtectedRoute = ({ children, allowedRoles }) => {
    const isAuthenticated = AuthService.isAuthenticated()
    const userRole = AuthService.getRole()

    console.log('ProtectedRoute Check:', {
        isAuthenticated,
        userRole,
        allowedRoles
    })

    if (!isAuthenticated) {
        console.log('Not authenticated, redirecting to login')
        return <Navigate to="/login" replace />
    }

    // Handle both "ADMIN" and "ROLE_ADMIN" formats
    const cleanRole = userRole ? userRole.replace('ROLE_', '') : ''

    if (allowedRoles && !allowedRoles.includes(cleanRole)) {
        console.log('User role not allowed:', cleanRole, 'Allowed:', allowedRoles)
        
        // Redirect to appropriate dashboard based on role
        switch(cleanRole) {
            case 'ADMIN':
                return <Navigate to="/admin-dashboard" replace />
            case 'MANAGER':
                return <Navigate to="/manager-dashboard" replace />
            case 'CASHIER':
            case 'CLIENT':
                return <Navigate to="/bank-user-dashboard" replace />
            default:
                return <Navigate to="/login" replace />
        }
    }

    console.log('Access granted to:', cleanRole)
    return children
}

export default ProtectedRoute