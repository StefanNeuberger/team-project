import { ShopList } from "../components/ShopList";

export default function Home() {

    return (
        <section className="flex flex-col flex-1 items-center justify-center py-8">
            <h1 className="text-3xl font-bold mb-8">🛍️ Shop Management</h1>
            <ShopList/>
        </section>
    )
}

