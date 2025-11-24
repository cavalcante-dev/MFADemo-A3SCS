import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import './MfaVerification.css';

const MfaVerification = ({ tmpToken, role, username, onBack }) => {
  const { verifyMfa } = useAuth();
  const [code, setCode] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    const result = await verifyMfa(tmpToken, code, username, role);

    if (!result.success) {
      setError(result.error || 'Código MFA inválido');
      setLoading(false);
    }
  };

  return (
    <div className="mfa-container">
      <div className="mfa-card">
        <h2>🔐 Verificação MFA</h2>
        <p className="subtitle">Digite o código de verificação</p>
        <p className="mfa-info">
          O código MFA foi exibido no console do servidor.
          <br />
          Verifique o terminal onde o backend está rodando.
        </p>

        <form onSubmit={handleSubmit} className="mfa-form">
          <div className="form-group">
            <label htmlFor="code">Código MFA</label>
            <input
              id="code"
              type="text"
              value={code}
              onChange={(e) => setCode(e.target.value.replace(/\D/g, '').slice(0, 6))}
              required
              maxLength="6"
              placeholder="000000"
              className="mfa-input"
            />
          </div>

          {error && <div className="error-message">{error}</div>}

          <div className="mfa-actions">
            <button type="button" onClick={onBack} className="btn-secondary">
              Voltar
            </button>
            <button type="submit" disabled={loading || code.length !== 6} className="btn-primary">
              {loading ? 'Verificando...' : 'Verificar'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default MfaVerification;

