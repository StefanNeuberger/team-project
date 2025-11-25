import { createBrowserRouter } from "react-router-dom";
import { ShopList } from "../components/ShopList";

const Home = () => (
  <section className="flex flex-col items-center justify-center min-h-screen py-8">
    <h1 className="text-3xl font-bold mb-8">🛍️ Shop Management</h1>
    <ShopList />
  </section>
);

export const router = createBrowserRouter([
  {
    path: "/",
    element: <Home />,
  },
]);
