import { useAuth } from '../context/AuthContext';
import './Navigation.css';

const Navigation = ({ currentPage, onNavigate }) => {
  const { user } = useAuth();

  const isAdmin = user?.role === 'ADMIN';
  const isManager = user?.role === 'MANAGER' || isAdmin;

  return (
    <nav className="navigation">
      <div className="nav-brand">
        <h3>🛡️ Zero Trust</h3>
      </div>
      <div className="nav-links">
        <button
          className={currentPage === 'dashboard' ? 'active' : ''}
          onClick={() => onNavigate('dashboard')}
        >
          Dashboard
        </button>
        <button
          className={currentPage === 'profile' ? 'active' : ''}
          onClick={() => onNavigate('profile')}
        >
          Perfil
        </button>
        {isManager && (
          <button
            className={currentPage === 'reports' ? 'active' : ''}
            onClick={() => onNavigate('reports')}
          >
            Relatórios
          </button>
        )}
        {isAdmin && (
          <button
            className={currentPage === 'admin' ? 'active' : ''}
            onClick={() => onNavigate('admin')}
          >
            Admin
          </button>
        )}
        <button
          className={currentPage === 'logs' ? 'active' : ''}
          onClick={() => onNavigate('logs')}
        >
          Logs
        </button>
      </div>
      <div className="nav-user">
        <span>{user?.username}</span>
        <span className="role-badge">{user?.role}</span>
      </div>
    </nav>
  );
};

export default Navigation;

