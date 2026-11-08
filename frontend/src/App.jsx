
import { useEffect, useMemo, useState } from 'react';
import {
  Activity,
  Bot,
  CheckCircle2,
  CircleAlert,
  ListTodo,
  Play,
  Plus,
  RefreshCw,
  Trash2,
  LogOut
} from 'lucide-react';

const API =
  import.meta.env.VITE_API_URL || 'http://localhost:8082';

export default function App() {
  const [username, setUsername] = useState('agentuser');
  const [password, setPassword] = useState('');
  const [loggedIn, setLoggedIn] = useState(false);

  const [tasks, setTasks] = useState([]);
  const [runs, setRuns] = useState([]);

  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [instruction, setInstruction] = useState('');

  const [busy, setBusy] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const auth = useMemo(
    () => `Basic ${btoa(`${username}:${password}`)}`,
    [username, password]
  );

  // Common API function
  async function api(path, options = {}) {
    const response = await fetch(`${API}${path}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        Authorization: auth,
        ...options.headers
      }
    });

    if (!response.ok) {
      let errorMessage = `HTTP ${response.status}`;

      try {
        const data = await response.json();
        errorMessage = data.error || errorMessage;
      } catch {}

      throw new Error(errorMessage);
    }

    if (response.status === 204) return null;

    return response.json();
  }

  // Login and verify credentials
  async function login(e) {
    e.preventDefault();
    setBusy(true);
    setError('');

    try {
      const taskData = await api('/api/tasks');

      setTasks(taskData);
      setLoggedIn(true);
      setMessage('Login successful');

    } catch (e) {
      setError(
        e.message === 'HTTP 401'
          ? 'Invalid username or password'
          : e.message
      );
    } finally {
      setBusy(false);
    }
  }

  // Load tasks and agent runs
  async function refresh() {
    try {
      const [taskData, runData] = await Promise.all([
        api('/api/tasks'),
        api('/api/agent/runs')
      ]);

      setTasks(taskData);
      setRuns(runData);
      setError('');

    } catch (e) {
      setError(e.message);
    }
  }

  useEffect(() => {
    if (loggedIn) {
      refresh();
    }
  }, [loggedIn]);

  // Logout
  function logout() {
    setLoggedIn(false);
    setPassword('');
    setTasks([]);
    setRuns([]);
    setMessage('');
    setError('');
  }

  // Create task
  async function create(e) {
    e.preventDefault();
    setBusy(true);

    try {
      await api('/api/tasks', {
        method: 'POST',
        body: JSON.stringify({
          title,
          description
        })
      });

      setTitle('');
      setDescription('');
      setMessage('Task created successfully');

      await refresh();

    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  // Update task status
  async function status(id, newStatus) {
    try {
      await api(`/api/tasks/${id}/status`, {
        method: 'PATCH',
        body: JSON.stringify({
          status: newStatus
        })
      });

      await refresh();

    } catch (e) {
      setError(e.message);
    }
  }

  // Delete task
  async function remove(id) {
    try {
      await api(`/api/tasks/${id}`, {
        method: 'DELETE'
      });

      await refresh();

    } catch (e) {
      setError(e.message);
    }
  }

  // Generate AI agent plan
  async function run(e) {
    e.preventDefault();
    setBusy(true);

    try {
      const result = await api('/api/agent/run', {
        method: 'POST',
        body: JSON.stringify({
          instruction
        })
      });

      setMessage(`Plan created: ${result.runId}`);
      setInstruction('');

      await refresh();

    } catch (e) {
      setError(e.message);
    } finally {
      setBusy(false);
    }
  }

  const done = tasks.filter(
    task => task.status === 'COMPLETED'
  ).length;

  // Login screen
  if (!loggedIn) {
    return (
      <div className="shell">
        <main>
          <div className="panel">
            <div className="brand">
              <span className="logo">
                <Bot />
              </span>
              <b>AI Sandbox Agent</b>
            </div>

            <h1>Welcome back</h1>
            <p>Login to access your workspace.</p>

            {error && (
              <div className="alert err">
                <CircleAlert />
                {error}
              </div>
            )}

            <form onSubmit={login}>
              <label>Username</label>

              <input
                type="text"
                value={username}
                onChange={e => setUsername(e.target.value)}
                required
                placeholder="Enter username"
              />

              <label>Password</label>

              <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                required
                placeholder="Enter Render APP_PASSWORD"
              />

              <button
                className="primary"
                type="submit"
                disabled={busy}
              >
                {busy ? 'Logging in...' : 'Login'}
              </button>
            </form>

            <small>
              Your password is entered locally in this browser
              and is not stored in the source code.
            </small>
          </div>
        </main>
      </div>
    );
  }

  // Main dashboard
  return (
    <div className="shell">

      <aside>
        <div className="brand">
          <span className="logo">
            <Bot />
          </span>
          <b>Sandbox Agent</b>
        </div>

        <div className="nav">
          <Activity /> Overview
        </div>

        <div className="nav">
          <ListTodo /> Tasks
        </div>

        <p className="sidefoot">● Connected workspace</p>
      </aside>

      <main>

        <header>
          <div>
            <small>YOUR WORKSPACE</small>
            <h1>Agent overview</h1>
            <p>
              Plan work, track tasks, and inspect agent runs.
            </p>
          </div>

          <div>
            <button
              className="secondary"
              onClick={refresh}
            >
              <RefreshCw size={16} /> Refresh
            </button>

            <button
              className="secondary"
              onClick={logout}
            >
              <LogOut size={16} /> Logout
            </button>
          </div>
        </header>

        {error && (
          <div className="alert err">
            <CircleAlert /> {error}
          </div>
        )}

        {message && (
          <div className="alert ok">
            <CheckCircle2 /> {message}
          </div>
        )}

        <section className="stats">
          <div>
            <span>Total tasks</span>
            <strong>{tasks.length}</strong>
          </div>

          <div>
            <span>Completed</span>
            <strong>{done}</strong>
          </div>

          <div>
            <span>Agent plans</span>
            <strong>{runs.length}</strong>
          </div>
        </section>

        <section className="cols">

          {/* Create task */}
          <div className="panel">
            <h2>Create a task</h2>
            <p>Keep your work organized.</p>

            <form onSubmit={create}>
              <label>Title</label>

              <input
                required
                maxLength="160"
                value={title}
                onChange={e => setTitle(e.target.value)}
                placeholder="e.g. Add task filters"
              />

              <label>Description</label>

              <textarea
                rows="3"
                value={description}
                onChange={e => setDescription(e.target.value)}
                placeholder="What needs to be done?"
              />

              <button
                className="primary"
                disabled={busy}
              >
                <Plus size={16} /> Add task
              </button>
            </form>
          </div>

          {/* AI Agent */}
          <div className="panel">
            <h2>Ask the agent</h2>
            <p>Generate a step-by-step plan.</p>

            <form onSubmit={run}>
              <label>Instruction</label>

              <textarea
                rows="5"
                required
                maxLength="2000"
                value={instruction}
                onChange={e => setInstruction(e.target.value)}
                placeholder="Plan a safe demo of my project..."
              />

              <button
                className="primary"
                disabled={busy}
              >
                <Play size={16} /> Generate plan
              </button>

              <small>
                Without an API key, deterministic demo mode is used.
              </small>
            </form>
          </div>

        </section>

        {/* Tasks list */}
        <section className="panel">
          <h2>Tasks</h2>

          {tasks.length === 0 ? (
            <p className="empty">
              No tasks yet. Create one above.
            </p>
          ) : (
            tasks.map(task => (
              <div className="task" key={task.id}>

                <div className="tasktext">
                  <b>{task.title}</b>

                  <p>
                    {task.description || 'No description'}
                  </p>

                  <small>
                    {new Date(task.createdAt).toLocaleString()}
                  </small>
                </div>

                <select
                  value={task.status}
                  onChange={e =>
                    status(task.id, e.target.value)
                  }
                >
                  <option>PENDING</option>
                  <option>RUNNING</option>
                  <option>COMPLETED</option>
                  <option>FAILED</option>
                </select>

                <button
                  className="delete"
                  onClick={() => remove(task.id)}
                  title="Delete"
                >
                  <Trash2 />
                </button>

              </div>
            ))
          )}
        </section>

        {/* Agent plans */}
        <section className="panel">
          <h2>Recent agent plans</h2>

          {runs.length === 0 ? (
            <p className="empty">No plans yet.</p>
          ) : (
            runs.slice().reverse().map(result => (
              <article className="run" key={result.runId}>

                <div className="runhead">
                  <b>{result.instruction}</b>
                  <span>{result.planningMode}</span>
                </div>

                <ol>
                  {result.steps.map((step, index) => (
                    <li key={index}>{step}</li>
                  ))}
                </ol>

                <small>
                  {new Date(result.createdAt).toLocaleString()}
                  {' · '}
                  {result.status}
                </small>

              </article>
            ))
          )}
        </section>

        <footer>
          AI Sandbox Agent · Learning prototype
        </footer>

      </main>
    </div>
  );
}
