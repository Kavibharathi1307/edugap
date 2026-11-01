import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../api/client';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const saved = localStorage.getItem('edugap_user');
    return saved ? JSON.parse(saved) : null;
  });
  const [token, setToken] = useState(() => localStorage.getItem('edugap_token'));
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function verifyUser() {
      if (token) {
        try {
          const res = await api.get('/auth/me');
          if (res.data?.data) {
            setUser(res.data.data);
            localStorage.setItem('edugap_user', JSON.stringify(res.data.data));
          }
        } catch (err) {
          console.error("Token verification failed", err);
          logout();
        }
      }
      setLoading(false);
    }
    verifyUser();
  }, [token]);

  const login = async (email, password) => {
    const res = await api.post('/auth/login', { email, password });
    const authData = res.data.data;
    setToken(authData.token);
    setUser({
      id: authData.studentId,
      fullName: authData.fullName,
      email: authData.email,
      role: authData.role
    });
    localStorage.setItem('edugap_token', authData.token);
    localStorage.setItem('edugap_user', JSON.stringify({
      id: authData.studentId,
      fullName: authData.fullName,
      email: authData.email,
      role: authData.role
    }));
    return authData;
  };

  const register = async (fullName, email, password) => {
    const res = await api.post('/auth/register', { fullName, email, password });
    const authData = res.data.data;
    setToken(authData.token);
    setUser({
      id: authData.studentId,
      fullName: authData.fullName,
      email: authData.email,
      role: authData.role
    });
    localStorage.setItem('edugap_token', authData.token);
    localStorage.setItem('edugap_user', JSON.stringify({
      id: authData.studentId,
      fullName: authData.fullName,
      email: authData.email,
      role: authData.role
    }));
    return authData;
  };

  const logout = () => {
    setToken(null);
    setUser(null);
    localStorage.removeItem('edugap_token');
    localStorage.removeItem('edugap_user');
  };

  return (
    <AuthContext.Provider value={{ user, token, loading, login, register, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}
