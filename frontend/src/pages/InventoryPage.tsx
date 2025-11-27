import { useParams } from "react-router-dom";

export default function InventoryPage() {
    const { shopId } = useParams();
    return <>Shop { shopId }</>
}