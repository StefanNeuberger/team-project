import { useParams } from "react-router-dom";

export default function ItemDetailPage() {
    const { shopId, id } = useParams();
    return <>Shop { shopId }, Detail { id }</>
}