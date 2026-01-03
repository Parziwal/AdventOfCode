use std::collections::HashMap;

advent_of_code::solution!(11);

#[derive(Debug, Clone, PartialEq, Eq, Hash)]
struct Vertex(String);

impl Vertex {
    fn new(id: &str) -> Vertex {
        Vertex(id.to_string())
    }
}

fn depth_first_search(
    memo: &mut HashMap<Vertex, u64>,
    vertex: &Vertex,
    target: &Vertex,
    graph: &HashMap<Vertex, Vec<Vertex>>,
) -> u64 {
    if let Some(&count) = memo.get(vertex) {
        return count;
    }

    if vertex == target {
        return 1;
    }

    if !graph.contains_key(vertex) {
        return 0;
    }

    let neighbours = &graph[vertex];
    let mut path_count = 0;
    for neighbour in neighbours {
        path_count += depth_first_search(memo, neighbour, target, graph);
    }

    memo.insert(vertex.clone(), path_count);
    path_count
}

fn parse_graph(input: &str) -> HashMap<Vertex, Vec<Vertex>> {
    input
        .lines()
        .map(|line| {
            let (vertex, neighbours) = line.split_once(":").unwrap();
            let vertex = Vertex::new(vertex);
            let neighbours = neighbours.split_whitespace().map(Vertex::new).collect();

            (vertex, neighbours)
        })
        .collect()
}

fn count_paths(graph: &HashMap<Vertex, Vec<Vertex>>, from: &str, to: &str) -> u64 {
    let mut memo = HashMap::with_capacity(graph.len());
    depth_first_search(&mut memo, &Vertex::new(from), &Vertex::new(to), graph)
}

pub fn part_one(input: &str) -> Option<u64> {
    let vertexes = parse_graph(input);

    let out_paths = count_paths(&vertexes, "you", "out");
    Some(out_paths)
}

pub fn part_two(input: &str) -> Option<u64> {
    let vertexes = parse_graph(input);

    let fft_paths = count_paths(&vertexes, "svr", "fft");
    let dac_paths = count_paths(&vertexes, "fft", "dac");
    let out_paths = count_paths(&vertexes, "dac", "out");

    Some(fft_paths * dac_paths * out_paths)
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test_part_one() {
        let result = part_one(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(5));
    }

    #[test]
    fn test_part_two() {
        let result = part_two(&advent_of_code::template::read_file("examples", DAY));
        assert_eq!(result, Some(2));
    }
}
