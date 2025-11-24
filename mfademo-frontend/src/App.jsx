import { useState } from 'react';
import { AuthProvider, useAuth } from './context/AuthContext';
import Login from './components/Login';
import MfaVerification from './components/MfaVerification';
import Navigation from './components/Navigation';
import Dashboard from './components/Dashboard';
import Profile from './components/Profile';
import Reports from './components/Reports';
import AdminPanel from './components/AdminPanel';
import Logs from './components/Logs';
import './App.css';

const AppContent = () => {
  const { isAuthenticated, loading } = useAuth();
  const [mfaState, setMfaState] = useState(null);
  const [currentPage, setCurrentPage] = useState('dashboard');

  if (loading) {
    return <div className="loading-screen">Carregando...</div>;
  }

  if (!isAuthenticated) {
    if (mfaState) {
      return (
        <MfaVerification
          tmpToken={mfaState.tmpToken}
          role={mfaState.role}
          username={mfaState.username}
          onBack={() => setMfaState(null)}
        />
      );
    }
    return <Login onMfaRequired={(tmpToken, role, username) => setMfaState({ tmpToken, role, username })} />;
  }

  const renderPage = () => {
    switch (currentPage) {
      case 'profile':
        return <Profile />;
      case 'reports':
        return <Reports />;
      case 'admin':
        return <AdminPanel />;
      case 'logs':
        return <Logs />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="app">
      <Navigation currentPage={currentPage} onNavigate={setCurrentPage} />
      <main className="main-content">
        {renderPage()}
      </main>
    </div>
  );
};

function App() {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
}

export default App;
