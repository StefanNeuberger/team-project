import { useFindAllItems } from "@/api/generated/items/items.ts";
import ItemsView from "@/components/ItemsView.tsx";

export default function Items() {
    const { data, isLoading } = useFindAllItems();

    console.log( data?.data )

    if ( isLoading ) {
        return <p>Loading</p>
    }


    return (

        <ItemsView items={ data?.data }/>

    )
}
