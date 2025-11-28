import { useFindAllItems } from "@/api/generated/items/items.ts";
import ItemsView from "@/components/ItemsView.tsx";
import Loading from "@/components/custom-ui/Loading.tsx";

export default function ItemsPage() {
    const { data, isLoading } = useFindAllItems();

    console.log( data?.data )


    return (
        <div className={ "flex flex-1 justify-center flex-col mt-8 items-center" }>
            {
                isLoading &&
                <div className={ "flex flex-col justify-center items-center gap-4" }>
                    <Loading text={ "Items loading..." }/>
                </div>
            }
            {
                data?.data &&
                <>
                    <h1 className={ "font-bold text-2xl text-accent-foreground underline" }>All
                        items</h1>
                    <ItemsView items={ data.data }/>
                </>
            }
        </div>

    )
}
