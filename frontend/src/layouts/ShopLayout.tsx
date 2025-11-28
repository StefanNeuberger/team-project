import Footer from "@/components/Footer.tsx";
import { Outlet } from "react-router-dom";
import Header from "@/components/Header.tsx";
import { Toaster } from "sonner";

export default function ShopLayout() {
  return (
    <>
      <Header />
      <div className="flex flex-col flex-1 items-center justify-center py-8">
        <Outlet />
      </div>
      <Toaster />
      <Footer />
    </>
  );
}
