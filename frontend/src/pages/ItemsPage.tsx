import { useParams } from "react-router-dom";

export default function ItemPage() {
    const { shopId } = useParams();
    return <>Shop { shopId }</>
}