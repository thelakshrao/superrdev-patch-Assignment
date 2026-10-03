import { useState, useEffect } from 'react';
import { fetchTasks } from '../api';

export function useTasks(query, status, page, pageSize) {
  const [tasks, setTasks] = useState([]);
  const [total, setTotal] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const controller = new AbortController();
    setLoading(true);
    setError(null);                              // CHANGE 1: new line

    fetchTasks({ query, status, page, pageSize }, controller.signal)
      .then((data) => {
        setTasks(data.items);
        setTotal(data.total);
        setLoading(false);
      })
      .catch((err) => {
        if (err.name === 'AbortError') return;   // keep this line as it is
        setError(err.message);
        setLoading(false);                       // CHANGE 2: new line
      });

    return () => controller.abort();
  }, [query, status, page, pageSize]);

  return { tasks, total, loading, error };
}