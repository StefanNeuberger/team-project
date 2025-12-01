import { useParams } from "react-router-dom";
import { useGetItemById } from "@/api/generated/items/items.ts";
import Loading from "@/components/custom-ui/Loading.tsx";
import ItemDetail from "@/components/ItemsPage Comps/ItemDetail.tsx";

export default function ItemDetailPage() {
    const { shopId, id } = useParams();
    const { data: itemData, isError, error, isLoading } = useGetItemById( id! );

    if ( isLoading ) {
        return <Loading classNames="flex flex-col justify-center items-center w-full"/>;
    }

    if ( isError || !itemData ) {
        throw error;
    }

    return <div className="p-8 flex flex-col justify-start items-center">
        <ItemDetail shopId={ shopId! } item={ itemData.data }/>
    </div>
}