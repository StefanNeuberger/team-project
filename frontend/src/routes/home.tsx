import { createBrowserRouter } from "react-router-dom";

const Home = () => (
  <section className="flex flex-col items-center justify-center h-screen">
    <h1 className="text-3xl font-bold">🥦 Hello World 🥦</h1>
  </section>
);

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
  },
]);
