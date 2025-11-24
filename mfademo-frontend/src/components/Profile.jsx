import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { userAPI } from '../services/api';
import './Profile.css';

const Profile = () => {
  const { user } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    try {
      setLoading(true);
      const data = await userAPI.getProfile();
      setProfile(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Carregando perfil...</div>;
  }

  if (error) {
    return <div className="error">Erro: {error}</div>;
  }

  return (
    <div className="profile-container">
      <h1>Meu Perfil</h1>
      
      {profile && (
        <div className="profile-card">
          <div className="profile-field">
            <label>ID:</label>
            <span>{profile.id}</span>
          </div>
          <div className="profile-field">
            <label>Usuário:</label>
            <span>{profile.username}</span>
          </div>
          <div className="profile-field">
            <label>Role:</label>
            <span className={`role-badge role-${profile.role.toLowerCase()}`}>
              {profile.role}
            </span>
          </div>
          <div className="profile-field">
            <label>Email:</label>
            <span>{profile.email || 'N/A'}</span>
          </div>
          <div className="profile-field">
            <label>Status:</label>
            <span>{profile.enabled ? '✅ Ativo' : '❌ Inativo'}</span>
          </div>
          <div className="profile-field">
            <label>MFA:</label>
            <span>{profile.mfaEnabled ? '✅ Habilitado' : '❌ Desabilitado'}</span>
          </div>
        </div>
      )}
    </div>
  );
};

export default Profile;

